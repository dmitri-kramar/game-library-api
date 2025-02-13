package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service layer for managing games. Provides basic CRUD operations.

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Optional<Game> getById(Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> getByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }
}
