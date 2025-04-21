# 🎮 Game Library API

A RESTful API for managing a video game library. Users can view games, developers, genres, and platforms. 
Admins can create, update, and delete them.

## 🚀 Technologies

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- H2 Database (in-memory)
- OpenAPI / Swagger UI
- Maven
- Lombok

## 📦 Getting Started

### Clone the repository

```bash
git clone https://github.com/dmitri-kramar/game-library-api.git
cd game-library-api
```

### Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at:  
`http://localhost:8080`

### Access Swagger UI

Open your browser and go to:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Use HTTP Basic Auth in Swagger to authorize.

## 🧪 Testing

To run all tests:

```bash
./mvnw test
```

- **Integration tests** validate real controller and service logic.
- **Unit tests** use mock dependencies for isolated testing.

## 🗄️ Database

The project uses an in-memory H2 database:

- Schema initialized via `schema.sql`
- Sample data preloaded via `data.sql`

## 🔐 Authentication

- Auth endpoints: `/register` and `/login`
- All other endpoints require authentication
- Use Swagger with Basic Auth

### 👤 User Roles

- `USER`: Can view entities and manage own account
- `ADMIN`: Full access to all data and user management

## 🔐 Security

- **Basic Authentication** with Spring Security
- **Password Hashing** using `BCryptPasswordEncoder` (never stores raw passwords)
- **Data Validation** via `@Valid` annotations and custom DTOs
- **Write-only fields** (e.g., passwords) using `@JsonProperty(access = WRITE_ONLY)` to prevent exposure in responses
- **Role-based access control** using `@PreAuthorize` and a `UserSecurityService` for ownership checks
- **Centralized exception handling** to avoid leaking stack traces and internal details


## 📄 OpenAPI

The API is documented with OpenAPI 3.  
Specification is available at: `/openapi.yaml`

---

© 2025 github.com/dmitri-kramar