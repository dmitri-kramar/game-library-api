package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Controller for handling game-related API requests.
*/

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Tag(name = "Games")
public class GameController {

    private final GameService gameService;

    /*
    Endpoint to get all games, accessible to authenticated users
    */

    @Operation(summary = "Get all games (USER)", description = "Retrieves a list of all games.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of games"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required")
    })
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAll());
    }

    /*
    Endpoint to get a game by ID, accessible to authenticated users
    */

    @Operation(summary = "Get a game by ID (USER)", description = "Retrieves a game by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the game by ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getById(id));
    }

    /*
    Endpoint to create a new game, accessible only by ADMIN role
    */

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game savedGame = gameService.save(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
    }

    /*
    Endpoint to create a new game, accessible only by ADMIN role
    */

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game game) {
        Game existingGame = gameService.getById(id);
        existingGame.setTitle(game.getTitle());
        existingGame.setDescription(game.getDescription());
        existingGame.setReleaseDate(game.getReleaseDate());
        existingGame.setDeveloper(game.getDeveloper());
        existingGame.setPlatforms(game.getPlatforms());
        existingGame.setGenres(game.getGenres());
        Game updatedGame = gameService.save(existingGame);
        return ResponseEntity.ok(updatedGame);
    }

    /*
    Endpoint to delete a game by ID, accessible only by ADMIN role
    */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
