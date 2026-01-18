package mqtt;

public final class MqttConfig {

    public static final String BROKER_URL = "tcp://localhost:1883";
    public static final String TASK_TOPIC = "scheduler/tasks";

    private MqttConfig() {}
}
