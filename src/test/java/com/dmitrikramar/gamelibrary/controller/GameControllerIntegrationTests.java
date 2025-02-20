package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GameControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        Developer testDeveloper = new Developer("TestDeveloper", null);
        Platform testPlatform = new Platform("TestPlatform", null);
        Genre testGenre = new Genre("TestGenre", null);

        developerRepository.save(testDeveloper);
        platformRepository.save(testPlatform);
        genreRepository.save(testGenre);

        // Creates a test game and saves it to the repository
        testGame = new Game("Test Game", LocalDate.parse("2000-01-01"), "Description",
                testDeveloper, new HashSet<>(Set.of(testPlatform)), new HashSet<>(Set.of(testGenre)));

        gameRepository.save(testGame);
    }

    @Test
    @WithMockUser
    void getAllGames_ShouldReturnListOfGames() throws Exception {
        // Sends a GET request to fetch all games and expects a list with at least one game
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testGame.getId() + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + testGame.getId() + ")].title").value("Test Game"));
    }

    @Test
    void getAllGames_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        // Sends a GET request to fetch all games but expects unauthorized
        // status (HTTP 401) when no authentication is provided
        mockMvc.perform(get("/games"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getGame_ShouldReturnGame_WhenGameExists() throws Exception {
        // Sends a GET request to fetch a specific game and expects the game's details to match
        mockMvc.perform(get("/games/{id}", testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void getGame_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        // Sends a GET request for a specific game but expects unauthorized
        // status (HTTP 401) when no authentication is provided
        mockMvc.perform(get("/games/{id}", testGame.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGame_ShouldReturnCreatedGame_WhenAdmin() throws Exception {
        Game newGame = new Game("New Game", LocalDate.parse("2000-01-01"), "Description",
                testGame.getDeveloper(), testGame.getPlatforms(), testGame.getGenres());

        // Sends a POST request to create a new game and expects it to return
        // a created response with the correct details
        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Game"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGame_ShouldReturnUpdatedGame_WhenAdmin() throws Exception {
        testGame.setTitle("Updated Title");

        // Updates the test game's title and sends a PUT request to update it in the database
        mockMvc.perform(put("/games/{id}", testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGame_ShouldReturnNoContent_WhenAdmin() throws Exception {
        // Sends a DELETE request to remove the test game and expects no content in the response
        mockMvc.perform(delete("/games/{id}", testGame.getId()))
                .andExpect(status().isNoContent());

        // Verifies that the game was actually deleted from the repository
        assertFalse(gameRepository.existsById(testGame.getId()));
    }
}
