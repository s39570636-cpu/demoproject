package com.smarthome.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class MqttService {
    private final Deque<String> recentMessages = new ConcurrentLinkedDeque<>();
    private MqttClient client;

    @PostConstruct
    public void init() {
        try {
            String broker = System.getProperty("mqtt.broker", "tcp://mosquitto:1883");
            client = new MqttClient(broker, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions opts = new MqttConnectOptions();
            opts.setAutomaticReconnect(true);
            opts.setCleanSession(true);
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) { System.err.println("MQTT lost: " + cause); }
                public void messageArrived(String topic, MqttMessage message) {
                    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                    String entry = System.currentTimeMillis() + " | " + topic + " | " + payload;
                    recentMessages.addFirst(entry);
                    while (recentMessages.size() > 500) recentMessages.removeLast();
                }
                public void deliveryComplete(IMqttDeliveryToken token) {}
            });
            client.connect(opts);
            client.subscribe("home/+/sensor/#");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void shutdown() {
        try { if (client != null && client.isConnected()) client.disconnect(); } catch (MqttException ignored) {}
    }

    public Deque<String> getRecentMessages() { return recentMessages; }

    public void publish(String topic, String payload) throws MqttException {
        if (client == null || !client.isConnected()) throw new IllegalStateException("MQTT not connected");
        client.publish(topic, new MqttMessage(payload.getBytes(StandardCharsets.UTF_8)));
    }
}