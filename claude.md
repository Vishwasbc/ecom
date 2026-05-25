# E-Commerce Microservices Project Memory

## Project Overview
This is a Spring Boot microservices-based e-commerce application with multiple independent services communicating through service discovery.

## Architecture
- **Service Discovery**: Eureka (Netflix OSS)
- **Pattern**: Microservices Architecture
- **Build Tool**: Maven (multi-module project)
- **Java Framework**: Spring Boot
- **Database**: PostgreSQL

## Services

### 1. Eureka Service
- **Purpose**: Service registry and discovery
- **Location**: `/eureka`
- **Type**: Spring Cloud Netflix Eureka Server

### 2. Order Service
- **Location**: `/order-service`
- **Purpose**: Handles order management and processing
- **Database**: PostgreSQL (orders_db)
- **Profiles**: 
  - `post` - PostgreSQL configuration
  - `mysql` - MySQL configuration (alternative)
- **Key Config**: 
  - URL: `jdbc:postgresql://localhost:5432/orders_db`
  - Username: `postgres`
  - Password: `vish@post`
  - DDL: `create-drop` (dev/test only)

### 3. Product Service
- **Location**: `/product-service`
- **Purpose**: Manages product catalog
- **Database**: PostgreSQL
- **Profiles**: `post` (PostgreSQL), `mysql` (MySQL alternative)

### 4. User Service
- **Location**: `/user-service`
- **Purpose**: User management and authentication
- **Database**: PostgreSQL
- **Profiles**: `post` (PostgreSQL), `mysql` (MySQL alternative)

## Technology Stack
- Spring Boot
- Spring Cloud (Eureka, Config Server)
- Spring Data JPA / Hibernate
- PostgreSQL / MySQL
- Maven

## Running the Application

### Prerequisites
- Java 11+ (Check your Spring Boot version)
- Maven 3.6+
- PostgreSQL running on `localhost:5432`
- Database: `orders_db` created

### VM Arguments Required
```
-Dspring.profiles.active=post
```

### Build
```bash
mvn clean install
```

### Run Individual Services
Each service can be run with the active profile set:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=post"
```

## Database Profiles
- **post**: PostgreSQL configuration
- **mysql**: MySQL configuration (alternative)

## Logging Configuration
- Hibernate SQL logging is enabled
- Bind parameters are logged at TRACE level
- SQL formatting and highlighting enabled for dev environment

## Important Notes
- DDL mode `create-drop` is for development/testing only
- Credentials in config files should be moved to environment variables for production
- Services register with Eureka for discovery

