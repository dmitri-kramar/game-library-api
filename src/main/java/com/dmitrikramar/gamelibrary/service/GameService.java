package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service class responsible for business logic related to Game entities.

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    // Retrieves a list of all games, including their related entities.
    public List<Game> getAll() {
        return gameRepository.findAllWithRelations();
    }

    // Retrieves a game by its ID, including related entities.
    // Throws an exception if the game is not found.
    public Game getById(Long id) {
        return gameRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));
    }

    // Saves a new or existing game to the repository.
    @Transactional
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    // Deletes a game by its ID.
    @Transactional
    public void deleteById(Long id) {
        gameRepository.delete(getById(id));
    }
}
