package mqtt;

import model.ScheduledTask;
import org.eclipse.paho.client.mqttv3.*;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.util.PriorityQueue;

public class Worker {

    private final PriorityQueue<ScheduledTask> queue = new PriorityQueue<>();

    public void start() throws MqttException {

        MqttClient client =
                new MqttClient(MqttConfig.BROKER_URL, "task-worker");

        client.connect();

        client.subscribe(MqttConfig.TASK_TOPIC, (topic, message) -> {
            ObjectInputStream ois =
                    new ObjectInputStream(
                            new ByteArrayInputStream(message.getPayload()));

            ScheduledTask task =
                    (ScheduledTask) ois.readObject();

            queue.add(task);
        });
    }

    public void runScheduler() throws Exception {

        while (true) {
            ScheduledTask next = queue.peek();
            Instant executeAtInstant = Instant.now().plusSeconds(100);
            if (next != null) {
                executeAtInstant =
                        next.getExecuteAt()
                                .atZone(ZoneId.systemDefault()) // use local timezone
                                .toInstant();
            }

            if (next != null && executeAtInstant.isBefore(Instant.now())) {
                queue.poll();
                next.getTask().execute();
            }

            Thread.sleep(500);
        }
    }
}
