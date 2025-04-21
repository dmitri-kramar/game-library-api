package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Game} entities.
 * Provides methods to fetch games along with their related entities.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Retrieves a game by its ID and eagerly loads its associated developer,
     * platforms, and genres using an entity graph.
     *
     * @param id the ID of the game
     * @return an {@link Optional} containing the game with its related entities, if found
     */
    @EntityGraph(attributePaths = {"developer", "platforms", "genres"})
    @Query("SELECT g FROM Game g WHERE g.id = :id")
    Optional<Game> findByIdWithRelations(@Param("id") Long id);

    /**
     * Retrieves all games and eagerly fetches their related developer, platforms, and genres using LEFT JOIN FETCH.
     *
     * @return a list of games with their related entities
     */
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.developer LEFT JOIN FETCH g.platforms LEFT JOIN FETCH g.genres")
    List<Game> findAllWithRelations();
}