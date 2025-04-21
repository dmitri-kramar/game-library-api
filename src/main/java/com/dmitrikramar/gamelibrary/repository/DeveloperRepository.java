package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Developer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link Developer} entities.
 * Extends {@link JpaRepository} to inherit standard JPA methods.
 */
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    /**
     * Retrieves a developer by its ID and eagerly loads its associated games using an entity graph.
     *
     * @param id the ID of the developer
     * @return an {@link Optional} containing the developer with its games, if found
     */
    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT d FROM Developer d WHERE d.id = :id")
    Optional<Developer> findByIdWithRelations(@Param("id") Long id);

    /**
     * Retrieves all developers and eagerly fetches their associated games using a LEFT JOIN FETCH.
     *
     * @return a list of developers with their games
     */
    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.games")
    List<Developer> findAllWithRelations();

    /**
     * Checks if a developer with the given name already exists.
     *
     * @param name the name of the developer
     * @return true if a developer with the given name exists, false otherwise
     */
    boolean existsByName(String name);
}
