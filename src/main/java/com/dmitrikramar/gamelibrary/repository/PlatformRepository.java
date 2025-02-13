package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for managing Platform entities in the database. Provides CRUD operations.

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
