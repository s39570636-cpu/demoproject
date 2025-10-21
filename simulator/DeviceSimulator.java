package com.smarthome.simulator;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.JsonObject;
import java.util.Random;

public class DeviceSimulator {
    public static void main(String[] args) throws Exception {
        String broker = "tcp://localhost:1883";
        MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
        client.connect();
        Random rnd = new Random();
        while (true) {
            double temp = 20 + rnd.nextGaussian() * 1.5;
            double light = Math.max(0, rnd.nextDouble() * 100);
            int motion = rnd.nextDouble() < 0.2 ? 1 : 0;
            JsonObject payload = new JsonObject();
            payload.addProperty("temperature", Math.round(temp * 10.0) / 10.0);
            payload.addProperty("light", Math.round(light * 10.0) / 10.0);
            payload.addProperty("motion", motion);
            String topic = "home/livingroom/sensor/state";
            MqttMessage msg = new MqttMessage(payload.toString().getBytes());
            client.publish(topic, msg);
            System.out.println("Published " + topic + " -> " + payload);
            Thread.sleep(4000);
        }
    }
}