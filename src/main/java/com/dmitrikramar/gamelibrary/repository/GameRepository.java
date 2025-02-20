package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for performing CRUD operations on Game entities.

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // Finds a Game by its id and fetches its related 'developer', 'platforms', and 'genres' attributes using an
    // entity graph. This ensures that the associated entities are loaded together with the Game entity in one query.
    @EntityGraph(attributePaths = {"developer", "platforms", "genres"})
    @Query("SELECT g FROM Game g WHERE g.id = :id")
    Optional<Game> findByIdWithRelations(@Param("id") Long id);

    // Finds all Games and eagerly fetches their related 'developer', 'platforms', and 'genres' using LEFT JOIN FETCH.
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.developer LEFT JOIN FETCH g.platforms LEFT JOIN FETCH g.genres")
    List<Game> findAllWithRelations();
}