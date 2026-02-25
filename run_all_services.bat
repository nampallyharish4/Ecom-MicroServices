@echo off
TITLE E-Commerce Microservices Startup Script
COLOR 0A

echo ======================================================================
echo           STARTING E-COMMERCE MICROSERVICES ECOSYSTEM
echo ======================================================================
echo.

:: 1. Eureka Server (Infrastructure)
echo [1/9] Starting EUREKA SERVER (Port 5000)...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
echo.
echo Waiting for Eureka to initialize (15 seconds)...
timeout /t 15 /nobreak > nul

:: 2. Core Services
echo [2/9] Starting USER SERVICE (Port 8050)...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"

echo [3/9] Starting PRODUCT SERVICE (Port 8051)...
start "Product Service" cmd /k "cd product-service && mvn spring-boot:run"

echo [4/9] Starting INVENTORY SERVICE (Port 8082)...
start "Inventory Service" cmd /k "cd inventory-service && mvn spring-boot:run"

echo.
echo Waiting for Core Services (15 seconds)...
timeout /t 15 /nobreak > nul

:: 3. Business Services
echo [5/9] Starting CART SERVICE (Port 8052)...
start "Cart Service" cmd /k "cd cart-service && mvn spring-boot:run"

echo [6/9] Starting FAVOURITE SERVICE (Port 8056)...
start "Favourite Service" cmd /k "cd favourite-service && mvn spring-boot:run"

echo [7/9] Starting ORDER SERVICE (Port 8053)...
start "Order Service" cmd /k "cd order-service && mvn spring-boot:run"

echo.
echo Waiting for Order Processing (10 seconds)...
timeout /t 10 /nobreak > nul

:: 4. Fulfillment Services
echo [8/9] Starting PAYMENT SERVICE (Port 8054)...
start "Payment Service" cmd /k "cd payment-service && mvn spring-boot:run"

echo [9/9] Starting SHIPPING SERVICE (Port 8055)...
start "Shipping Service" cmd /k "cd shipping-service && mvn spring-boot:run"

echo.
echo ======================================================================
echo ALL STARTUP COMMANDS SENT SUCCESSFULLY
echo ======================================================================
echo.
echo 1. Check the new windows for individual service logs.
echo 2. Eureka Dashboard: http://localhost:5000
echo 3. Keep these windows open while testing endpoints.
echo.
pause
