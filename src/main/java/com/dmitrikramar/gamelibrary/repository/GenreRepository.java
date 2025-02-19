package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Genre;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for managing Genre entities in the database. Provides CRUD operations.

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT g FROM Genre g WHERE g.id = :id")
    Optional<Genre> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.games")
    List<Genre> findAllWithRelations();
}
