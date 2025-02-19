package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Platform;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for managing Platform entities in the database. Provides CRUD operations.

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT p FROM Platform p WHERE p.id = :id")
    Optional<Platform> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT p FROM Platform p LEFT JOIN FETCH p.games")
    List<Platform> findAllWithRelations();
}
