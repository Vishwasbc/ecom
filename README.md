~~# E-Commerce Microservices Platform

A Spring Boot-based microservices architecture for an e-commerce platform with service discovery, order management, product catalog, and user management.

## 📋 Project Overview

This project implements a distributed e-commerce system using Spring Boot and Spring Cloud. It includes multiple independent microservices that communicate with each other, with Eureka as the service discovery mechanism.

### Services

- **Eureka Server** - Service discovery and registration
- **Order Service** - Manages orders and order processing
- **Product Service** - Manages product catalog and inventory
- **User Service** - Manages user accounts and authentication

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────┐
│              API Gateway / Load Balancer             │
└─────────────────────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
    ┌────────┐     ┌──────────┐    ┌─────────┐
    │ Order  │     │ Product  │    │  User   │
    │Service │     │ Service  │    │Service  │
    └────────┘     └──────────┘    └─────────┘
        │               │               │
        └───────────────┼───────────────┘
                        │
            ┌──────────────────────┐
            │   Eureka Server      │
            │ (Service Discovery)  │
            └──────────────────────┘
```

## 🛠️ Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **MySQL** (for persistence)
- **Git**

## 📦 Project Structure

```
ecom/
├── eureka/                 # Service Discovery Server
│   ├── src/
│   ├── pom.xml
│   └── ...
├── order-service/          # Order Management Service
│   ├── src/
│   ├── pom.xml
│   └── ...
├── product-service/        # Product Catalog Service
│   ├── src/
│   ├── pom.xml
│   └── ...
├── user-service/           # User Management Service
│   ├── src/
│   ├── pom.xml
│   └── ...
├── pom.xml                 # Parent POM (Multi-module project)
└── README.md
```

## 🚀 Getting Started

### 1. Clone or Setup the Project

```bash
cd C:\Users\vijet\projects\ecom
```

### 2. Build All Services

From the root directory:

```bash
./mvnw clean install
```

Or on Windows:

```bash
mvnw.cmd clean install
```

### 3. Start Eureka Server First

```bash
cd eureka
../mvnw.cmd spring-boot:run
```

The Eureka dashboard will be available at: `http://localhost:8761`

### 4. Start Individual Services

In separate terminals:

**Order Service:**
```bash
cd order-service
../mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```
Port: `8083`

**Product Service:**
```bash
cd product-service
../mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

**User Service:**
```bash
cd user-service
../mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

> **Note:** Replace `mysql` with `post` if using PostgreSQL or omit the argument to use the default profile.

## 📡 API Endpoints

### Cart API

**Add to Cart**
```bash
curl --location 'http://localhost:8083/api/cart' \
--header 'X-User-ID: 1' \
--header 'Content-Type: application/json' \
--data '{
    "productId":"1",
    "quantity":3
}'
```

**Get Cart**
```bash
curl --location 'http://localhost:8083/api/cart' \
--header 'X-User-ID: 1'
```

**Delete Product from Cart**
```bash
curl --location --request DELETE 'http://localhost:8083/api/cart/items/1' \
--header 'X-User-ID: 1'
```

### Orders API

**Place Order**
```bash
curl --location --request POST 'http://localhost:8083/api/orders' \
--header 'X-User-ID: 1'
```

## 🗄️ Database Configuration

Each service can be configured with different profiles:

- `application.yml` - Default configuration
- `application-mysql.yml` - MySQL configuration
- `application-post.yml` - PostgreSQL configuration

### Setting Active Profile via VM Arguments

When running services, specify the active profile using Maven VM arguments:

**Using MySQL:**
```bash
./mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

**Using PostgreSQL:**
```bash
./mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=post"
```

**Using Default Profile:**
```bash
./mvnw.cmd spring-boot:run
```

Alternatively, set the environment variable before running:
```powershell
$env:SPRING_PROFILES_ACTIVE="mysql"
./mvnw.cmd spring-boot:run
```

## 📝 Configuration

### Eureka Configuration

Update `eureka/src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: eureka-server
  
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: true
```

### Service Configuration

Each service should register with Eureka. Example configuration:

```yaml
spring:
  application:
    name: order-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## 🔒 Authentication

The API uses `X-User-ID` header for user identification. Include this header in all requests.

## 🧪 Testing

Run tests for a specific service:

```bash
cd order-service
../mvnw.cmd test
```

Run all tests:

```bash
./mvnw.cmd test
```

## 📚 Technologies Used

- **Spring Boot** 3.x - Application framework
- **Spring Cloud** - Microservices framework
- **Spring Cloud Netflix Eureka** - Service discovery
- **Maven** - Build tool
- **Java 21** - Programming language
- **MySQL/PostgreSQL** - Database

## 🐛 Troubleshooting

### Services not registering with Eureka
- Ensure Eureka server is running on port 8761
- Check that `eureka.client.service-url.defaultZone` is correctly configured
- Verify network connectivity between services

### Port conflicts
- Check if ports 8761, 8083, etc. are already in use
- Update port configurations in `application.yml` files

### Database connection issues
- Verify database is running and accessible
- Check database credentials in configuration files
- Ensure required databases exist

---

**Last Updated:** April 2026





