#!/usr/bin/env bash
# start-all.sh — build, start services and run simulator
# Usage: ./start-all.sh [--no-docker] [--no-sim]
set -euo pipefail

NO_DOCKER=false
NO_SIM=false

for arg in "$@"; do
  case $arg in
    --no-docker) NO_DOCKER=true ;;
    --no-sim) NO_SIM=true ;;
    *) ;;
  esac
done

ROOT_DIR="$(cd ""$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "Project root: $ROOT_DIR"

# 1) Build backend JAR
echo "Building backend..."
cd "$ROOT_DIR/backend"
mvn clean package -DskipTests

# 2) Start infrastructure and backend via Docker Compose (unless disabled)
if [ "$NO_DOCKER" = false ]; then
  echo "Starting docker-compose services (db, mosquitto, backend, ai)..."
  cd "$ROOT_DIR"
docker-compose up --build -d
echo "Waiting for services to initialize..."
sleep 8
else
  echo "Skipping docker-compose (as requested). Ensure DB/MQTT are running manually."
fi

# 3) If you prefer running backend locally (instead of Docker), uncomment below:
# echo "Starting backend locally..."
# cd "$ROOT_DIR/backend"
# mvn spring-boot:run &

# 4) Run device simulator (unless disabled)
if [ "$NO_SIM" = false ]; then
  echo "Starting Java device simulator (Maven exec). Logs will stream to console."
  cd "$ROOT_DIR"
  # Run simulator in foreground so you can see logs; run with --no-sim to skip.
mvn -q -f backend/pom.xml exec:java -Dexec.mainClass="com.smarthome.simulator.DeviceSimulator"
else
  echo "Skipping simulator run (as requested)."
fi
