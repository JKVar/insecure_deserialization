import mqtt.Worker;

public class VulnerableScheduler {

    public static void main(String[] args) throws Exception {
        Worker worker = new Worker();
        worker.start();
        worker.runScheduler();
    }
}

