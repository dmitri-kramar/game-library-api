package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for managing Developer entities in the database. Provides CRUD operations.

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
