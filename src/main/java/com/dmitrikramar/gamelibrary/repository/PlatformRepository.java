package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Platform;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for performing CRUD operations on Platform entities.

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    // Finds a Platform by its id and fetches its related 'games' attribute using an entity graph.
    // This ensures that the 'games' collection is loaded along with the Platform entity.
    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT p FROM Platform p WHERE p.id = :id")
    Optional<Platform> findByIdWithRelations(@Param("id") Long id);

    // Finds all Platforms and fetches their related 'games' collection eagerly using a LEFT JOIN FETCH.
    @Query("SELECT p FROM Platform p LEFT JOIN FETCH p.games")
    List<Platform> findAllWithRelations();
}
