package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for managing Review entities in the database. Provides CRUD operations.

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
