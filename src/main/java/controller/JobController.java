package controller;

import auth.Role;
import auth.User;
import model.*;
import tasks.CleanupTask;
import tasks.ReportTask;

import java.time.LocalDateTime;

public class JobController {

    public void submitJob(TaskRequest request, User user) throws Exception {

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("Only admins can schedule jobs");
        }

        Task task;
//        if ("CLEANUP".equals(request.taskType)) {
//            task = new CleanupTask(request.parameter);
//        } else {
//            throw new IllegalArgumentException("Unknown task type");
//        }
        switch (request.taskType) {
            case "CLEANUP":
                task = new CleanupTask(request.parameter);
                break;
            case "REPORT":
                task = new ReportTask(request.parameter);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type");
        }

//        ScheduledTask scheduledTask = new ScheduledTask(user, task);
//        LocalDateTime executeAt = LocalDateTime.now().plusMinutes(2);
//        ScheduledTask scheduledTask = new ScheduledTask(executeAt, user, task);
//        TaskQueue.enqueue(scheduledTask);
    }
}
