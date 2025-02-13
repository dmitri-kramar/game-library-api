package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository interface for managing Game entities in the database. Provides CRUD operations.

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByTitle(String title);
}