# ECom Microservices Project

**Overview**: Monolith converted into separate Spring Boot microservices (product, user, order). Each service uses an H2 database for local development.

**Services**
- **product-service**: REST API for products; H2 datasource; default port 8081.
- **user-service**: REST API for users and addresses; H2 datasource; default port 8082.
- **order-service**: Holds orders and cart functionality; H2 datasource; default port 8083.

**Project Layout**
- **Parent POM**: Maven multi-module root that aggregates the three services.
- **Module structure**: Each service follows Controller / DTO / Model / Repository / Service / service.impl / utility package layout.

**What's Done**
- **Split**: Code moved into `product-service`, `user-service`, and `order-service` modules.
- **Order + Cart**: Cart implementation consolidated into `order-service` and reorganized into packages.
- **H2 Configs**: Each service has an `application.yml` configured for H2 in-memory DB for easy local testing.

**Remaining / Next Steps**
- **Inter-service communication**: Add Feign/WebClient to call product/user services from `order-service` (price lookup, user validation).
- **Common module**: Optionally extract shared DTOs/entities into a `common` module to avoid duplication.
- **Build & verify**: Run `mvn -DskipTests package` per module and fix compile issues if any.

**Quick Local Build**
```powershell
cd order-service
mvn -DskipTests package
```



