package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import tools.jackson.databind.ObjectMapper;

public class SafeTaskProducer {

    private final MqttClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public SafeTaskProducer() throws MqttException {
        client = new MqttClient(MqttConfig.BROKER_URL, "task-producer");
        client.connect();
    }

    public void publish(byte[] payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload);
        client.publish(MqttConfig.TASK_TOPIC, message);
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
