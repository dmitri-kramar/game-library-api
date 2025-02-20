package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class GameServiceIntegrationTests {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GenreRepository genreRepository;


    private Game testGame;

    @BeforeEach
    void setUp() {
        // Creates and saves test developer, platform, and genre before each test
        Developer testDeveloper = new Developer("TestDeveloper", null);
        Platform testPlatform = new Platform("TestPlatform", null);
        Genre testGenre = new Genre("TestGenre", null);

        developerRepository.save(testDeveloper);
        platformRepository.save(testPlatform);
        genreRepository.save(testGenre);

        // Creates and saves a test game
        testGame = new Game("Test Game", LocalDate.parse("2000-01-01"), "Description",
                testDeveloper, new HashSet<>(Set.of(testPlatform)), new HashSet<>(Set.of(testGenre)));

        gameRepository.save(testGame);
    }

    @Test
    void getAll() {
        // Verifies that the service returns all games, including the test game
        List<Game> games = gameService.getAll();
        assertThat(games).isNotEmpty();
        assertThat(games).contains(testGame);
    }

    @Test
    void getById() {
        // Verifies that the service retrieves the correct game by ID
        Game game = gameService.getById(testGame.getId());
        assertThat(game).isEqualTo(testGame);
    }

    @Test
    void getById_ShouldThrowException_WhenGameNotFound() {
        // Verifies that an exception is thrown when the game with the provided ID doesn't exist
        Long nonExistentId = 999L;
        assertThrows(NoSuchElementException.class, () -> gameService.getById(nonExistentId));
    }

    @Test
    void save() {
        // Verifies that the game is saved correctly and has an ID
        Game savedGame = gameService.save(testGame);
        assertThat(savedGame).isNotNull();
        assertThat(savedGame.getId()).isNotNull();
    }

    @Test
    void deleteById() {
        // Verifies that the game is deleted correctly and no longer exists in the repository
        Long idToDelete = testGame.getId();
        gameService.deleteById(idToDelete);
        assertThrows(NoSuchElementException.class, () -> gameService.getById(idToDelete));
    }
}

