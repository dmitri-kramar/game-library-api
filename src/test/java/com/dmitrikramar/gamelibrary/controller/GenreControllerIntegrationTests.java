package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GenreControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreRepository genreRepository;

    private Genre testGenre;

    @BeforeEach
    void setUp() {
        // Initializes a test genre before each test and saves it to the repository
        testGenre = new Genre("TestGenre", null);
        genreRepository.save(testGenre);
    }

    @Test
    @WithMockUser
    void getAllGenres_ShouldReturnListOfGenres() throws Exception {
        // Sends a GET request to fetch all genres and expects a list with at least one genre
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testGenre.getId() + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + testGenre.getId() + ")].name").value("TestGenre"));
    }

    @Test
    void getAllGenres_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        // Sends a GET request to fetch all genres but expects unauthorized
        // status (HTTP 401) when no authentication is provided
        mockMvc.perform(get("/genres"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getGenre_ShouldReturnGenre_WhenGenreExists() throws Exception {
        // Sends a GET request to fetch a specific genre and expects the genre's details to match
        mockMvc.perform(get("/genres/{id}", testGenre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestGenre"));
    }

    @Test
    void getGenre_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        // Sends a GET request for a specific genre but expects unauthorized
        // status (HTTP 401) when no authentication is provided
        mockMvc.perform(get("/genres/{id}", testGenre.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGenre_ShouldReturnCreatedGenre_WhenAdmin() throws Exception {
        Genre newGenre = new Genre("NewGenre", null);

        // Sends a POST request to create a new genre and expects
        // it to return a created response with the correct details
        mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGenre)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewGenre"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGenre_ShouldReturnUpdatedGenre_WhenAdmin() throws Exception {
        testGenre.setName("Updated Genre");

        // Updates the test genre's name and sends a PUT request to updateName it in the database
        mockMvc.perform(put("/genres/{id}", testGenre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGenre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Genre"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGenre_ShouldReturnNoContent_WhenAdmin() throws Exception {
        // Sends a DELETE request to remove the test genre and expects no content in the response
        mockMvc.perform(delete("/genres/{id}", testGenre.getId()))
                .andExpect(status().isNoContent());

        // Verifies that the genre was actually deleted from the repository
        assertFalse(genreRepository.existsById(testGenre.getId()));
    }
}
