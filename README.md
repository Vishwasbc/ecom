# ECom Spring Boot Application

## Overview
This is a Spring Boot based e-commerce application. It provides RESTful APIs for managing products and users, including CRUD operations and mapping between DTOs and entities.

## Features
- Product management (create, update)
- User management
- DTO to entity mapping
- Repository pattern
- Service layer with @Transactional annotation for data integrity

## Technologies Used
- Java
- Spring Boot
- Spring Data JPA
- Lombok

## Recent Updates
- Added @Transactional annotation to service implementation classes for better transaction management.
- Improved code comments for clarity and maintainability.

## How to Run
1. Ensure Java and Maven are installed.
2. Run `mvnw.cmd spring-boot:run` from the project root.

## Directory Structure
- `src/main/java/com/example/ecom/` - Main source code
- `src/test/java/com/example/ecom/` - Test cases
- `README.md` - Project documentation

## Notes
- Use the provided DTOs for API requests and responses.
- Service methods are annotated with @Transactional for consistency.

## Contact
For issues, please open a GitHub issue or contact the maintainer.
