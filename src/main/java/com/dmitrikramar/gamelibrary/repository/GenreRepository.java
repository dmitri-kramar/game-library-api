package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for managing Genre entities in the database. Provides CRUD operations.

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
