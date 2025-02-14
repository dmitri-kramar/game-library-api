package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller for handling game-related API requests.

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.getAll();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game savedGame = gameService.save(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game game) {
        return gameService.getById(id)
                .map(existingGame -> {
                    existingGame.setTitle(game.getTitle());
                    existingGame.setDescription(game.getDescription());
                    existingGame.setReleaseDate(game.getReleaseDate());
                    existingGame.setDeveloper(game.getDeveloper());
                    existingGame.setPlatforms(game.getPlatforms());
                    existingGame.setGenres(game.getGenres());
                    Game updatedGame = gameService.save(existingGame);
                    return ResponseEntity.ok(updatedGame);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        if (gameService.existsById(id)) {
            gameService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
