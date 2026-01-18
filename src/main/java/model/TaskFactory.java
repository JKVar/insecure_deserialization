package model;

import tasks.CleanupTask;
import tasks.ReportTask;

public class TaskFactory {
    public static Task createTask(String taskType) {
        switch (taskType.toUpperCase()) {
            case "CLEANUP":
                return new CleanupTask();
            case "REPORT":
                return new ReportTask();
            default:
                throw new IllegalArgumentException("Unknown task type: " + taskType);
        }
    }
}
