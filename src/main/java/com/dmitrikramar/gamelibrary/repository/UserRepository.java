package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository interface for performing CRUD operations on User entities.

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds a User by its username.
    // Returns an Optional<User> to handle the case where the user may not exist.
    Optional<User> findByUsername(String username);

    // Checks if a User with the given username exists in the database.
    boolean existsByUsername(String username);
}
