# E-Commerce Microservices Ecosystem

## Project Overview
A cloud-native e-commerce application built with Spring Boot microservices, Netflix Eureka service discovery, and MySQL databases.

## Architecture
- **Language**: Java 19 (GraalVM CE 22.3.1)
- **Framework**: Spring Boot 3.x, Spring Cloud 2025.0.1
- **Database**: MySQL 8.0 (one database per service)
- **Service Discovery**: Netflix Eureka
- **Inter-service communication**: OpenFeign

## Service Catalog

| Service | Port | Database |
|---------|------|----------|
| Eureka Server (Web UI) | 5000 | N/A |
| User Service | 8050 | ecomuserms |
| Product Service | 8051 | ecomproductms |
| Cart Service | 8052 | ecomcartms |
| Order Service | 8053 | ecomorderms |
| Payment Service | 8054 | ecompaymentms |
| Shipping Service | 8055 | ecomshippingms |
| Favourite Service | 8056 | ecomfavouritems |
| Inventory Service | 8082 | inventory_db |

## File Structure
```
/
├── eureka-server/          # Service registry (Spring Cloud Netflix Eureka)
├── user-service/           # User identity management
├── product-service/        # Product catalog
├── cart-service/           # Shopping cart
├── order-service/          # Order processing
├── payment-service/        # Payment handling
├── shipping-service/       # Shipping logistics
├── favourite-service/      # User wishlists
├── inventory-service/      # Inventory management
├── config-server/          # Centralized config (optional)
├── start.sh                # Main startup script
└── logs/                   # Service logs (runtime, gitignored)
```

## Startup Script (start.sh)
The `start.sh` script:
1. Initializes MySQL data directory if needed
2. Starts MySQL on port 3306 with socket at `/home/runner/mysql-run/mysql.sock`
3. Starts Eureka Server on port 5000 (primary web interface)
4. Starts all microservices in dependency order with wait periods
5. Monitors MySQL health in a loop

## MySQL Configuration
- Data dir: `/home/runner/mysql-data`
- Socket: `/home/runner/mysql-run/mysql.sock`
- Port: 3306
- Username: root / Password: root
- Databases auto-created on first service startup via `createDatabaseIfNotExist=true`

## Eureka Dashboard
Available at port 5000 - shows all registered services and their health status.

## Service Logs
Each service logs to `/home/runner/workspace/logs/<service-name>.log`

## Build
All services use Maven. Pre-built JARs are in each service's `target/` directory.
To rebuild a specific service: `cd <service-dir> && mvn package -DskipTests`

## Notes
- Eureka was reconfigured to run on port 5000 (from default 8761) for Replit compatibility
- All services updated to register with Eureka at port 5000
- MySQL is started as part of the workflow (not a separate system service)
