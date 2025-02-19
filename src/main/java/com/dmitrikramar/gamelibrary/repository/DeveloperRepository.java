package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Developer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for managing Developer entities in the database. Provides CRUD operations.

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT d FROM Developer d WHERE d.id = :id")
    Optional<Developer> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.games")
    List<Developer> findAllWithRelations();
}
