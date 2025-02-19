package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for managing Game entities in the database. Provides CRUD operations.

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @EntityGraph(attributePaths = {"developer", "platforms", "genres"})
    @Query("SELECT g FROM Game g WHERE g.id = :id")
    Optional<Game> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.developer LEFT JOIN FETCH g.platforms LEFT JOIN FETCH g.genres")
    List<Game> findAllWithRelations();
}