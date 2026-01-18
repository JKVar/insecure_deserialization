import auth.AuthenticationService;
import auth.User;
import model.ScheduledTask;
import model.Task;
import mqtt.TaskProducer;
import tasks.CleanupTask;
import tasks.ReportTask;

import java.time.LocalDateTime;
import java.util.Scanner;

public class AddSchedule {

    public static void main(String[] args) throws Exception {
        AuthenticationService authService = new AuthenticationService();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String passwd = scanner.nextLine(); // Reads an integer
        User user = authService.authenticate(username, passwd);

        if (user == null) {
            System.out.println("Invalid credentials");
            scanner.close();
            return;
        }

        System.out.println("Your role is: " + user.getRole());
        boolean exit = false;

        TaskProducer taskProducer = new TaskProducer();
        Task task = null;
        do {
            System.out.println("\nChoose a task:");
            System.out.println(" [1] Clean directory\n [2] Generate report \n [0] Exit");

            int chosen = scanner.nextInt();

            switch (chosen) {
                case 1:
                    task = new CleanupTask("/temp");
                    break;
                case 2:
                    task = new ReportTask("You will be fired!");
                    break;
                default:
                    exit = true;
            }

            if (!exit) {
                ScheduledTask scheduledTask= new ScheduledTask(LocalDateTime.now().plusSeconds(5), user.getUsername(), task);
                taskProducer.publish(scheduledTask);
            }
        } while (!exit);

        taskProducer.close();
        scanner.close();
    }
}
