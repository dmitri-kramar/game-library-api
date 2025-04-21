package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.GameDTO;
import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing games.
 * Provides endpoints to create, retrieve, update, and delete games.
 */
@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    /**
     * Retrieves a list of all games.
     * Accessible only to authenticated users.
     *
     * @return list of games with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAll());
    }

    /**
     * Retrieves a game by its ID.
     * Accessible only to authenticated users.
     *
     * @param id the ID of the game
     * @return the game data with HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getById(id));
    }

    /**
     * Creates a new game.
     * Accessible only to users with ADMIN role.
     *
     * @param dto the game data
     * @return the created game with HTTP 201 status
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameDTO dto) {
        Game savedGame = gameService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
    }

    /**
     * Updates an existing game by ID.
     * Accessible only to users with ADMIN role.
     *
     * @param id  the ID of the game to update
     * @param dto the updated game data
     * @return the updated game with HTTP 200 status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @Valid @RequestBody GameDTO dto) {
        Game updatedGame = gameService.updateById(id, dto);
        return ResponseEntity.ok(updatedGame);
    }

    /**
     * Deletes a game by its ID.
     * Accessible only to users with ADMIN role.
     *
     * @param id the ID of the game to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
