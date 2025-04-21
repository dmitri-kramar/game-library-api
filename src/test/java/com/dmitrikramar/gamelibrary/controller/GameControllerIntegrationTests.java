package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.GameDTO;
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
    private Developer testDeveloper;
    private Platform testPlatform;
    private Genre testGenre;

    @BeforeEach
    void setUp() {
        testDeveloper = new Developer("Test Developer");
        testPlatform = new Platform("Test Platform");
        testGenre = new Genre("Test Genre");

        developerRepository.save(testDeveloper);
        platformRepository.save(testPlatform);
        genreRepository.save(testGenre);

        testGame = new Game("Test Game", LocalDate.parse("2000-01-01"), "Description",
                testDeveloper, new HashSet<>(Set.of(testPlatform)), new HashSet<>(Set.of(testGenre)));

        gameRepository.save(testGame);
    }

    @Test
    @WithMockUser
    void getAllGames_ShouldReturnListOfGames() throws Exception {
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testGame.getId() + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + testGame.getId() + ")].title").value("Test Game"));
    }

    @Test
    void getAllGames_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/games"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getGame_ShouldReturnGame_WhenGameExists() throws Exception {
        mockMvc.perform(get("/games/{id}", testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void getGame_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/games/{id}", testGame.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGame_ShouldReturnCreatedGame_WhenAdmin() throws Exception {
        GameDTO newGame = new GameDTO("New Game", LocalDate.parse("2000-01-01"), "Description", testDeveloper.getId(),
                new HashSet<>(Set.of(testPlatform.getId())), new HashSet<>(Set.of(testGenre.getId())));

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Game"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGame_ShouldReturnUpdatedGame_WhenAdmin() throws Exception {
        GameDTO updatedGame = new GameDTO("Updated Title", LocalDate.parse("2000-01-01"), "Description", testDeveloper.getId(),
                new HashSet<>(Set.of(testPlatform.getId())), new HashSet<>(Set.of(testGenre.getId())));

        mockMvc.perform(put("/games/{id}", testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGame_ShouldReturnNoContent_WhenAdmin() throws Exception {
        mockMvc.perform(delete("/games/{id}", testGame.getId()))
                .andExpect(status().isNoContent());

        assertFalse(gameRepository.existsById(testGame.getId()));
    }
}
