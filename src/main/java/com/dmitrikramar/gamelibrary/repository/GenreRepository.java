package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Genre;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for performing CRUD operations on Genre entities.

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Finds a Genre by its id and fetches its related 'games' attribute using an entity graph.
    // This ensures that the 'games' collection is loaded along with the Genre entity.
    @EntityGraph(attributePaths = {"games"})
    @Query("SELECT g FROM Genre g WHERE g.id = :id")
    Optional<Genre> findByIdWithRelations(@Param("id") Long id);

    // Finds all Genres and fetches their related 'games' collection eagerly using a LEFT JOIN FETCH.
    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.games")
    List<Genre> findAllWithRelations();
}
