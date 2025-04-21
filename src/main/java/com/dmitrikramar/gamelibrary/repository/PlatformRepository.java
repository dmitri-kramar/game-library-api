package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Platform;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Platform} entities.
 * Provides methods to retrieve platforms with their associated games.
 */
@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    /**
     * Retrieves a platform by its ID and eagerly loads its associated games using an entity graph.
     *
     * @param id the ID of the platform
     * @return an {@link Optional} containing the platform with its related games, if found
     */
    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT p FROM Platform p WHERE p.id = :id")
    Optional<Platform> findByIdWithRelations(@Param("id") Long id);

    /**
     * Retrieves all platforms and eagerly fetches their related games using LEFT JOIN FETCH.
     *
     * @return a list of platforms with their related games
     */
    @Query("SELECT p FROM Platform p LEFT JOIN FETCH p.games")
    List<Platform> findAllWithRelations();

    /**
     * Checks if a platform with the given name already exists.
     *
     * @param name the name to check
     * @return true if a platform with the specified name exists, false otherwise
     */
    boolean existsByName(String name);
}
