package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Genre;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Genre} entities.
 * Provides methods to retrieve genres with their related games.
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    /**
     * Retrieves a genre by its ID and eagerly loads its associated games using an entity graph.
     *
     * @param id the ID of the genre
     * @return an {@link Optional} containing the genre with its related games, if found
     */
    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT g FROM Genre g WHERE g.id = :id")
    Optional<Genre> findByIdWithRelations(@Param("id") Long id);

    /**
     * Retrieves all genres and eagerly fetches their related games using LEFT JOIN FETCH.
     *
     * @return a list of genres with their related games
     */
    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.games")
    List<Genre> findAllWithRelations();

    /**
     * Checks if a genre with the given name already exists.
     *
     * @param name the name to check
     * @return true if a genre with the specified name exists, false otherwise
     */
    boolean existsByName(String name);
}
