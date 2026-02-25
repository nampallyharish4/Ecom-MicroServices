#!/bin/bash
set -e

MYSQL_DATA="/home/runner/mysql-data"
MYSQL_TMP="/home/runner/mysql-tmp"
MYSQL_RUN="/home/runner/mysql-run"
MYSQL_SOCKET="$MYSQL_RUN/mysql.sock"
LOG_DIR="/home/runner/workspace/logs"

mkdir -p "$MYSQL_DATA" "$MYSQL_TMP" "$MYSQL_RUN" "$LOG_DIR"

echo "=== Starting E-Commerce Microservices ==="

# --- MySQL Setup ---
echo "[1/2] Setting up MySQL..."

if [ ! -d "$MYSQL_DATA/mysql" ]; then
  echo "    Initializing MySQL data directory..."
  mysqld --initialize-insecure --user=runner --datadir="$MYSQL_DATA" --tmpdir="$MYSQL_TMP" 2>&1
fi

if ! mysqladmin --socket="$MYSQL_SOCKET" ping --silent 2>/dev/null; then
  echo "    Starting MySQL server..."
  mysqld --user=runner \
    --datadir="$MYSQL_DATA" \
    --tmpdir="$MYSQL_TMP" \
    --socket="$MYSQL_SOCKET" \
    --pid-file="$MYSQL_RUN/mysql.pid" \
    --port=3306 \
    --mysqlx=OFF \
    --log-error="$MYSQL_DATA/mysql-error.log" \
    >> "$LOG_DIR/mysql.log" 2>&1 &

  echo "    Waiting for MySQL to be ready..."
  for i in $(seq 1 30); do
    if mysqladmin --socket="$MYSQL_SOCKET" ping --silent 2>/dev/null; then
      echo "    MySQL is ready."
      break
    fi
    sleep 1
  done

  # Set root password if not already set
  mysql --socket="$MYSQL_SOCKET" -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; FLUSH PRIVILEGES;" 2>/dev/null || true
else
  echo "    MySQL already running."
fi

# --- Start Spring Boot Services in order ---
echo "[2/2] Starting Spring Boot microservices..."

start_service() {
  local name=$1
  local jar=$2
  local port=$3
  local wait_secs=${4:-5}

  echo "    Starting $name on port $port..."
  java -jar "$jar" >> "$LOG_DIR/$name.log" 2>&1 &
  sleep "$wait_secs"
  echo "    $name started (PID: $!)"
}

# 1. Eureka Server (primary web interface on port 5000)
start_service "eureka-server" \
  /home/runner/workspace/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar \
  5000 15

# 2. Core services
start_service "user-service" \
  /home/runner/workspace/user-service/target/user-service-0.0.1-SNAPSHOT.jar \
  8050 10

start_service "product-service" \
  /home/runner/workspace/product-service/target/product-service-0.0.1-SNAPSHOT.jar \
  8051 10

start_service "inventory-service" \
  /home/runner/workspace/inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar \
  8082 10

# 3. Business logic services
start_service "cart-service" \
  /home/runner/workspace/cart-service/target/cart-service-0.0.1-SNAPSHOT.jar \
  8052 10

start_service "favourite-service" \
  /home/runner/workspace/favourite-service/target/favourite-service-0.0.1-SNAPSHOT.jar \
  8056 10

start_service "order-service" \
  /home/runner/workspace/order-service/target/order-service-0.0.1-SNAPSHOT.jar \
  8053 10

# 4. Fulfillment services
start_service "payment-service" \
  /home/runner/workspace/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar \
  8054 10

start_service "shipping-service" \
  /home/runner/workspace/shipping-service/target/shipping-service-0.0.1-SNAPSHOT.jar \
  8055 10

echo ""
echo "=== All services started ==="
echo "  Eureka Dashboard: http://localhost:5000"
echo "  User Service:     http://localhost:8050"
echo "  Product Service:  http://localhost:8051"
echo "  Cart Service:     http://localhost:8052"
echo "  Order Service:    http://localhost:8053"
echo "  Payment Service:  http://localhost:8054"
echo "  Shipping Service: http://localhost:8055"
echo "  Favourite Service:http://localhost:8056"
echo "  Inventory Service:http://localhost:8082"
echo ""
echo "Service logs available in: $LOG_DIR/"
echo ""

# Keep running and monitor MySQL
echo "Monitoring services... Press Ctrl+C to stop all."
while true; do
  if ! mysqladmin --socket="$MYSQL_SOCKET" ping --silent 2>/dev/null; then
    echo "WARNING: MySQL appears to be down. Attempting restart..."
    mysqld --user=runner \
      --datadir="$MYSQL_DATA" \
      --tmpdir="$MYSQL_TMP" \
      --socket="$MYSQL_SOCKET" \
      --pid-file="$MYSQL_RUN/mysql.pid" \
      --port=3306 \
      --mysqlx=OFF \
      >> "$LOG_DIR/mysql.log" 2>&1 &
    sleep 5
  fi
  sleep 30
done
