# ECom Spring Boot Application

## Project Overview
A sample e-commerce backend built with Java, Spring Boot, and Maven. It demonstrates layered architecture, DTO usage, and RESTful API design for user management.

## Code Structure
```
com.example.ecom
├── controller      # REST controllers (UserController)
├── service         # Service interfaces and implementations
├── repository      # Spring Data JPA repositories
├── model           # Entity classes (User, Address, UserRole)
├── dto             # Data Transfer Objects (UserRequest, UserResponse, AddressDTO)
├── utility         # Utility classes (UserMapper)
```

## Setup Instructions
1. **Prerequisites:**
   - Java 17+
   - Maven 3.6+

2. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd ecom
   ```

3. **Build the project:**
   ```sh
   mvn clean install
   ```

4. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The app will start on `http://localhost:8080` by default.

## API Endpoints

### User Management
Base path: `/api/users`

#### 1. Get All Users
- **GET** `/api/users`
- **Response:** `List<UserResponse>`

#### 2. Get User by ID
- **GET** `/api/users/{id}`
- **Response:** `UserResponse` (404 if not found)

#### 3. Add User
- **POST** `/api/users`
- **Request Body:** `UserRequest`
- **Response:** `201 Created` (or appropriate response)

#### 4. Update User
- **PUT** `/api/users/{id}`
- **Request Body:** `UserRequest`
- **Response:** `200 OK` if updated, `404 Not Found` if user does not exist

## DTOs
- `UserRequest`: Used for creating/updating users
- `UserResponse`: Used for returning user data
- `AddressDTO`: Used for address data in requests/responses

## Extending & Testing
- Add more endpoints in `controller` as needed
- Write tests in `src/test/java/com/example/ecom`
- Use `UserMapper` in `utility` for DTO/entity conversions

## Notes
- Follows industry standards for Spring Boot applications
- Uses Lombok for boilerplate reduction
- All business logic is in the service layer
- No direct entity exposure in API

---
For questions or contributions, open an issue or pull request.

