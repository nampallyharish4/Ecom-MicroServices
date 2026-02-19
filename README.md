# E-Commerce Microservices Project

A robust, distributed e-commerce application built using **Spring Boot** and **Spring Cloud**. This project demonstrates microservices patterns including service discovery, inter-service communication via OpenFeign, and independent database management.

## 🚀 Architecture Overview

This project follows a microservices architecture where each business capability is encapsulated in its own service with its own dedicated database.

### Core Services

1.  **Service Registry (Eureka Server)**: acts as the phonebook for all services.
2.  **User Service**: Manages accounts and credentials.
3.  **Product Service**: Handles the product catalog and inventory.
4.  **Cart Service**: Manages temporary shopping sessions.
5.  **Order Service**: Orchestrates the conversion of carts into orders.
6.  **Payment Service**: Processes financial transactions.
7.  **Shipping Service**: Manages logistics and delivery status.
8.  **Favourite Service**: handles user wishlists.

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Cloud (Eureka, OpenFeign, LoadBalancer)**
- **MySQL Server** (Persistence)
- **Lombok** (Boilerplate reduction)
- **MapStruct** (Service-DTO mapping)
- **Maven** (Build Tool)

## 📡 Service Port Mapping

| Service           | Port | Responsibility              |
| :---------------- | :--- | :-------------------------- |
| Eureka Server     | 8761 | Discovery Registry          |
| User Service      | 8050 | User & Auth Management      |
| Product Service   | 8051 | Catalog Management          |
| Cart Service      | 8052 | Shopping Cart Management    |
| Order Service     | 8053 | Order Processing            |
| Payment Service   | 8054 | Transaction Processing      |
| Shipping Service  | 8055 | Logistics & Status Tracking |
| Favourite Service | 8056 | Wishlist Management         |

## 🏁 Getting Started

### Prerequisites

- Install **Java 17** or higher.
- Install **Maven**.
- **MySQL Server** running on `localhost:3306` (User: `root`, Pass: `root`).

### Startup Sequence

To ensure smooth communication, start the services in this order:

1.  **Eureka Server**: Navigate to `eureka-server` and run `mvn spring-boot:run`.
2.  **Base Services**: Start `user-service` and `product-service`.
3.  **Business Services**: Start `cart-service`, `favourite-service`, and `order-service`.
4.  **Finalization Services**: Start `payment-service` and `shipping-service`.

## 🧪 Testing the Flow

For detailed testing steps and example JSON payloads, refer to the [TESTING_ENDPOINTS.txt](./TESTING_ENDPOINTS.txt) file in the root directory.

### High-Level Test Path:

1.  **Register User** -> `POST /api/users`
2.  **Create Product** -> `POST /products`
3.  **Add to Cart** -> `POST /cart`
4.  **Place Order** -> `POST /orders`
5.  **Make Payment** -> `POST /payments`
6.  **Ship Order** -> `POST /shipping`

## 🛡️ Key Features

- **Decoupled Databases**: Each service has its own database schema, ensuring true independence.
- **Service Discovery**: Services find each other dynamically via Eureka.
- **Feign Clients**: Type-safe REST clients for seamless inter-service communication.
- **Centralized Logging**: Consistent logging across all layers using SLF4J and Logback.
