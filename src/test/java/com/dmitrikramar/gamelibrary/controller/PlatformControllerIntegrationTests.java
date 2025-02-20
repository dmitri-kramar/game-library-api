package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Platform;
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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PlatformControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlatformRepository platformRepository;

    private Platform testPlatform;

    @BeforeEach
    void setUp() {
        testPlatform = new Platform("TestPlatform", null);
        platformRepository.save(testPlatform);
    }

    @Test
    @WithMockUser
    void getAllPlatforms_ShouldReturnListOfPlatforms() throws Exception {
        mockMvc.perform(get("/platforms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testPlatform.getId() + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + testPlatform.getId() + ")].name").value("TestPlatform"));
    }

    @Test
    void getAllPlatforms_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/platforms"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getPlatform_ShouldReturnPlatform_WhenPlatformExists() throws Exception {
        mockMvc.perform(get("/platforms/{id}", testPlatform.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestPlatform"));
    }

    @Test
    void getPlatform_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/platforms/{id}", testPlatform.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPlatform_ShouldReturnCreatedPlatform_WhenAdmin() throws Exception {
        Platform newPlatform = new Platform("NewPlatform", null);

        mockMvc.perform(post("/platforms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlatform)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewPlatform"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePlatform_ShouldReturnUpdatedPlatform_WhenAdmin() throws Exception {
        testPlatform.setName("Updated Platform");

        mockMvc.perform(put("/platforms/{id}", testPlatform.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlatform)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Platform"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePlatform_ShouldReturnNoContent_WhenAdmin() throws Exception {
        mockMvc.perform(delete("/platforms/{id}", testPlatform.getId()))
                .andExpect(status().isNoContent());

        assertFalse(platformRepository.existsById(testPlatform.getId()));
    }
}
