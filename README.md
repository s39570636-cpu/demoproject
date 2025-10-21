# Smart Home Automation System (Java) with AI

This repository contains a starter Java (Spring Boot) project for a Smart Home Automation System with AI integration points. It includes:

- backend/ (Spring Boot application, MQTT integration, REST API)
- simulator/ (device simulator using Eclipse Paho)
- ai/ (training script outline and model server stub)
- frontend/ (React app skeleton placeholder)
- docker-compose.yml and Dockerfile

See docs in each folder for running instructions.

Quickstart
---------
1. Build backend: cd backend && mvn clean package
2. Start services locally with Docker Compose: docker-compose up --build
3. Run simulator: mvn -q -f backend/pom.xml exec:java -Dexec.mainClass="com.smarthome.simulator.DeviceSimulator" (or run the built jar simulator)