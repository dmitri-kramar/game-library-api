package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.GenreDTO;
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
        testGenre = new Genre("Test Genre");
        genreRepository.save(testGenre);
    }

    @Test
    @WithMockUser
    void getAllGenres_ShouldReturnListOfGenres() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testGenre.getId() + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + testGenre.getId() + ")].name").value("Test Genre"));
    }

    @Test
    void getAllGenres_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getGenre_ShouldReturnGenre_WhenGenreExists() throws Exception {
        mockMvc.perform(get("/genres/{id}", testGenre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Genre"));
    }

    @Test
    void getGenre_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/genres/{id}", testGenre.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGenre_ShouldReturnCreatedGenre_WhenAdmin() throws Exception {
        GenreDTO newGenre = new GenreDTO("New Genre");

        mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGenre)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Genre"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGenre_ShouldReturnUpdatedGenre_WhenAdmin() throws Exception {
        GenreDTO updatedGenre = new GenreDTO("Updated Genre");

        mockMvc.perform(put("/genres/{id}", testGenre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGenre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Genre"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGenre_ShouldReturnNoContent_WhenAdmin() throws Exception {
        mockMvc.perform(delete("/genres/{id}", testGenre.getId()))
                .andExpect(status().isNoContent());

        assertFalse(genreRepository.existsById(testGenre.getId()));
    }
}
