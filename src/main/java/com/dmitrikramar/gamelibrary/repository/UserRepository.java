package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for managing User entities in the database. Provides CRUD operations.

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
