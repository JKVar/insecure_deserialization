import mqtt.SafeWorker;

public class SafeScheduler {

    public static void main(String[] args) throws Exception {
        SafeWorker worker = new SafeWorker();
        worker.start();
        worker.runScheduler();
    }
}
