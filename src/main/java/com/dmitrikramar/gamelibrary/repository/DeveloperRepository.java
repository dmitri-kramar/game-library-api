package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Developer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for performing CRUD operations on Developer entities.

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    // Finds a Developer by its id and fetches its related 'games' attribute using an entity graph.
    // This ensures that the associated 'games' are loaded together with the Developer entity in one query.
    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT d FROM Developer d WHERE d.id = :id")
    Optional<Developer> findByIdWithRelations(@Param("id") Long id);

    // Finds all Developers and eagerly fetches their related 'games' using LEFT JOIN FETCH.
    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.games")
    List<Developer> findAllWithRelations();

    boolean existsByName(String name);
}
