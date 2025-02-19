package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service layer for managing games. Provides basic CRUD operations.

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public List<Game> getAll() {
        return gameRepository.findAllWithRelations();
    }

    public Game getById(Long id) {
        return gameRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));
    }

    @Transactional
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    @Transactional
    public void deleteById(Long id) {
        gameRepository.delete(getById(id));
    }
}
