import dto.TaskMessageDTO;
import auth.AuthenticationService;
import auth.User;
import mqtt.SafeTaskProducer;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Scanner;

public class SafeAddSchedule {

    public static void main(String[] args) throws Exception {
        AuthenticationService authService = new AuthenticationService();
        Scanner scanner = new Scanner(System.in);
        ObjectMapper mapper = new ObjectMapper();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String passwd = scanner.nextLine();

        User user = authService.authenticate(username, passwd);

        if (user == null) {
            System.out.println("Invalid credentials");
            scanner.close();
            return;
        }

        System.out.println("Your role is: " + user.getRole());

        boolean exit = false;
        SafeTaskProducer taskProducer = new SafeTaskProducer();

        do {
            System.out.println("\nChoose a task:");
            System.out.println(" [1] Clean directory");
            System.out.println(" [2] Generate report");
            System.out.println(" [0] Exit");

            int chosen = scanner.nextInt();
            scanner.nextLine();

            String taskType = null;

            switch (chosen) {
                case 1:
                    taskType = "CLEANUP";
                    break;
                case 2:
                    taskType = "REPORT";
                    break;
                case 0:
                    exit = true;
                    continue;
                default:
                    System.out.println("Invalid choice");
                    continue;
            }

            TaskMessageDTO dto = new TaskMessageDTO();
            dto.username = user.getUsername();
            dto.taskType = taskType;
            dto.executeAtEpochMillis = Instant.now().plusSeconds(5).toEpochMilli();

            byte[] payload = mapper.writeValueAsBytes(dto);
            taskProducer.publish(payload);

            System.out.println("Task scheduled: " + taskType + " for user " + user.getUsername());

        } while (!exit);

        taskProducer.close();
        scanner.close();
    }
}
