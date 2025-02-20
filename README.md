# Video Game Library API

A RESTful API for managing a video game library, allowing authenticated users to interact 
with games, developers, genres, platforms, and user profiles.

## Features

- **User Management**:
    - Authenticated users can view the list of games, developers, genres, and platforms, 
      as well as individual entries for each.
    - Users can view their own profile and update their password or delete their account.
    - Administrators have the ability to manage the full set of users, and to add, 
      update, and delete entities (games, developers, genres, platforms).

- **Standard CRUD Operations**:
    - `GET` all resources or specific entries by ID.
    - `POST` to create new entities.
    - `PUT` to update existing entities.
    - `DELETE` to remove resources.

## Entities

- **Developer**: Represents a game developer.
- **Game**: Represents a video game.
- **Genre**: Represents a genre of games.
- **Platform**: Represents the platform on which a game can be played.
- **Role**: Represents the role of a user (e.g., ADMIN, USER).
- **User**: Represents the user of the application.

DTOs (Data Transfer Objects) are used for handling sensitive information and preventing 
unnecessary exposure of sensitive fields like passwords.

## Technologies Used

- **Backend**:
    - Java 17
    - Spring Boot
    - Spring Security
    - Hibernate
    - JPA (Java Persistence API)
    - MySQL
    - Maven
    - Lombok

- **Security**:
    - Basic authentication using `BCryptPasswordEncoder` for password hashing.
    - Stateless session management with `SessionCreationPolicy.STATELESS`.
    - Data validation using `@Valid` and custom DTOs to ensure sensitive data is not exposed.

© https://github.com/dmitri-kramar