package tasks;

import model.Task;

public class CleanupTask implements Task {

    private String directory;

    public CleanupTask() { this.directory = ""; }

    public CleanupTask(String directory) {
        this.directory = directory;
    }

    @Override
    public void execute() {
        System.out.println("Cleaning directory: " + directory);
    }

    @Override
    public void setParam(Object param) {
        this.directory = (String) param;
    }
}
