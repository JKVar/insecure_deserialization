package mqtt;

import model.ScheduledTask;
import org.eclipse.paho.client.mqttv3.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class TaskProducer {

    private final MqttClient client;

    public TaskProducer() throws MqttException {
        client = new MqttClient(MqttConfig.BROKER_URL, "task-producer");
        client.connect();
    }

    public void publish(ScheduledTask task) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(task);

        MqttMessage msg = new MqttMessage(bos.toByteArray());
        client.publish(MqttConfig.TASK_TOPIC, msg);
    }

    public void close() {
        if (client.isConnected()) {
            try {
                client.disconnect();
                client.close();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
