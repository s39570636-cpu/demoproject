package com.smarthome.controller;

import com.smarthome.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

import java.util.Deque;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DeviceController {
    private final MqttService mqttService;

    public DeviceController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @GetMapping("/recent")
    public Deque<String> recent() { return mqttService.getRecentMessages(); }

    @PostMapping("/actuate")
    public Map<String,String> actuate(@RequestBody Map<String,String> body) throws MqttException {
        String device = body.get("device");
        String action = body.get("action");
        if (device == null || action == null) throw new IllegalArgumentException("device and action required");
        String topic = "home/" + device + "/actuator/set";
        mqttService.publish(topic, action);
        return Map.of("status","sent","topic",topic);
    }
}