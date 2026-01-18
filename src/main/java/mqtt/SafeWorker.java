package mqtt;

import auth.AuthenticationService;
import auth.Role;
import auth.User;
import dto.TaskMessageDTO;
import model.ScheduledTask;
import model.Task;
import model.TaskFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.ZoneId;
import java.util.PriorityQueue;

public class SafeWorker {

    private final PriorityQueue<ScheduledTask> queue = new PriorityQueue<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public void start() throws MqttException {
        MqttClient client = new MqttClient(MqttConfig.BROKER_URL, "safe-task-worker");
        client.connect();

        client.subscribe(MqttConfig.TASK_TOPIC, (topic, message) -> {
            try {
                TaskMessageDTO dto = mapper.readValue(message.getPayload(), TaskMessageDTO.class);
                AuthenticationService authService = new AuthenticationService();

                if (dto.username == null || dto.taskType == null) {
                    System.err.println("Invalid task message");
                    return;
                }

                User user = authService.findUser(dto.username);
                if (user == null) {
                    return;
                }

                Task task = TaskFactory.createTask(dto.taskType);

                switch (dto.taskType) {
                    case "CLEANUP":
                        task.setParam("/temp");
                        break;
                    case "REPORT":
                        task.setParam("You are fired!");
                        break;
                }

                ScheduledTask scheduledTask =
                        new ScheduledTask(
                                Instant.ofEpochMilli(dto.executeAtEpochMillis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime(),
                                dto.username,
                                task
                        );

                queue.add(scheduledTask);

            } catch (Exception e) {
                System.err.println("The task could not be processed!");
            }
        });

        System.out.println("Safe worker started, waiting for tasks...");
    }

    public void runScheduler() throws InterruptedException {
        while (true) {
            ScheduledTask next = queue.peek();
            Instant executeAtInstant = Instant.now().plusSeconds(100);

            if (next != null) {
                executeAtInstant =
                        next.getExecuteAt()
                                .atZone(ZoneId.systemDefault())
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
