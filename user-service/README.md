# User Service

## Overview
The User Service is a core microservice within the GetMyFood platform responsible for user management, authentication, and authorization. It provides secure user registration and login functionality using JWT (JSON Web Tokens) for session management.

## API Endpoints

### 1. Register a New User
Creates a new user account in the system.

- **URL:** `/user/signup`
- **Method:** `POST`
- **Auth Required:** No

**Request Body:**

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890",
  "password": "securePassword123"
}
```

**Validation Rules:**
- `name`: Must not be blank.
- `email`: Must be a valid email format and not blank.
- `phoneNumber`: Must be exactly 10 digits.
- `password`: Must not be blank.

**Success Response:**
- **Code:** `201 CREATED`
- **Body:** Empty

**Error Responses:**
- **Code:** `400 BAD_REQUEST`
  - Body: `"Email already registered"` OR `"Phone Number already registered"`

- **Code:** `400 BAD_REQUEST`
  - **Body (Validation Error):**
    ```json
    {
      "errors": [
        "Invalid email format",
        "Phone number must be exactly 10 digits"
      ]
    }
    ```

**Example cURL:**
```bash
curl -X POST http://localhost:8080/user/signup   -H "Content-Type: application/json"   -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "1234567890",
    "password": "securePassword123"
  }'
```

---

### 2. User Login
Authenticates a user and returns a JWT token for authorized access to other endpoints.

- **URL:** `/user/login`
- **Method:** `POST`
- **Auth Required:** No

**Request Body:**

```json
{
  "loginId": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Success Response:**
- **Code:** `200 OK`
- **Body:** `"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."` (The JWT token string)

**Error Responses:**
- **Code:** `400 BAD_REQUEST`
  - Body: `"Invalid Login details"` OR `"Incorrect Password"`

**Example cURL:**
```bash
curl -X POST http://localhost:8080/user/login   -H "Content-Type: application/json"   -d '{
    "loginId": "john.doe@example.com",
    "password": "securePassword123"
  }'
```

**JWT Token Usage:**
The returned JWT token is intended to be included in the `Authorization` header of subsequent requests to protected endpoints (to be implemented in future phases).

```http
Authorization: Bearer <your_jwt_token_here>
```

---

## Database Schema

**Table: `users`**

| Column       | Type          | Constraints                         | Description                              |
|--------------|--------------|-------------------------------------|------------------------------------------|
| id           | BIGINT       | PRIMARY KEY, AUTO_INCREMENT         | Unique user identifier                   |
| user_name    | VARCHAR(255) | NOT NULL                            | Full name of the user                    |
| phone_number | VARCHAR(255) | NOT NULL, UNIQUE                    | User's phone number (10 digits)          |
| email        | VARCHAR(255) | NOT NULL, UNIQUE                    | User's email address                     |
| password     | VARCHAR(255) | NOT NULL                            | BCrypt-hashed password                   |
| role         | VARCHAR(255) | NOT NULL                            | User role (CUSTOMER, RESTAURANT_OWNER, ADMIN) |
| status       | VARCHAR(255) | NOT NULL                            | Account status (ACTIVE, DEACTIVATED, VERIFICATION_PENDING) |
| created_at   | DATETIME     | NOT NULL, updatable=false           | Timestamp of account creation            |
| updated_at   | DATETIME     | NOT NULL                            | Timestamp of last update                 |

---

## Key Features
- **JWT Authentication:** Generates secure tokens containing the user's ID, email, and role.
- **Password Encryption:** Uses BCryptPasswordEncoder to hash passwords before storing them in the database.
- **Data Validation:** Comprehensive input validation using Jakarta Bean Validation.
- **Unique User Enforcement:** Ensures email and phone number are unique across the platform.
- **Global Exception Handling:** Consistent and informative error responses for all API endpoints.
- **OpenAPI Documentation:** Automatic API documentation available at `/swagger-ui.html`.

---

## Configuration
The service is configured via the `application.yml` file. The following properties must be set:

| Property                 | Description                            | Example |
|---------------------------|----------------------------------------|---------|
| spring.datasource.url    | JDBC URL of the MySQL database         | jdbc:mysql://localhost:3306/getmyfood_user |
| spring.datasource.username | Database username                    | root    |
| spring.datasource.password | Database password                    | root    |
| jwt.secret               | Secret key for signing JWT tokens. Must be kept secure. | Passed as environment variable `JWT_SECRET` |
| jwt.expiration           | Expiration time for JWT tokens in ms   | 3600000 (1 hour) |

⚠️ **Important:** The `jwt.secret` should be injected as an environment variable (`JWT_SECRET`) in production environments. The value in the config file is a fallback for development.

---

## Build & Run

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Steps
1. Clone the repository and navigate to the `user-service` directory.
2. Configure the database: Ensure MySQL is running and the database `getmyfood_user` exists.
3. Set the JWT Secret (Optional for Dev):

```bash
export JWT_SECRET="yourSuperSecretKeyThatIs32charsOrMore"
```

4. Build the project:
```bash
mvn clean compile
```

5. Run the service:
```bash
mvn spring-boot:run
```

The service will start on `http://localhost:8080`.

---

## Dependencies
This service is currently independent and has no runtime dependencies on other microservices within the GetMyFood platform.  
It requires:
- **MySQL Database:** For persisting user data.
- **(Future) Kafka:** For publishing user-related events (e.g., `UserRegisteredEvent`).

---

## Technology Stack
- **Framework:** Spring Boot 3.3.5
- **Database:** MySQL with Spring Data JPA
- **Security:** Spring Security with BCrypt password encoding
- **Authentication:** JWT (JJWT library)
- **API Documentation:** Springdoc OpenAPI (Swagger UI)
- **Build Tool:** Maven
- **Code Quality:** Spotless for code formatting, JaCoCo for test coverage

---

## Development

### Code Formatting
The project uses Spotless for code formatting. To format code:

```bash
mvn spotless:apply
```

### Test Coverage
Test coverage reports are generated using JaCoCo. After running tests:

```bash
mvn test
```

View the report at: `target/site/jacoco/index.html`

---

This README provides a complete guide for any developer to understand, use, and run the User Service.
