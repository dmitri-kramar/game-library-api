package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
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
class DeveloperControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeveloperRepository developerRepository;

    private Developer testDeveloper;

    @BeforeEach
    void setUp() {
        testDeveloper = new Developer("TestDeveloper", null);
        developerRepository.save(testDeveloper);
    }

    @Test
    @WithMockUser
    void getAllDevelopers_ShouldReturnListOfDevelopers() throws Exception {
        mockMvc.perform(get("/developers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testDeveloper.getId() + ")]").exists())
                .andExpect(jsonPath("$[?(@.id == " + testDeveloper.getId() + ")].name").value("TestDeveloper"));
    }

    @Test
    void getAllDevelopers_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/developers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getDeveloper_ShouldReturnDeveloper_WhenDeveloperExists() throws Exception {
        mockMvc.perform(get("/developers/{id}", testDeveloper.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestDeveloper"));
    }

    @Test
    void getDeveloper_ShouldReturnUnauthorized_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/developers/{id}", testDeveloper.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDeveloper_ShouldReturnCreatedDeveloper_WhenAdmin() throws Exception {
        Developer newDeveloper = new Developer("NewDeveloper", null);

        mockMvc.perform(post("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDeveloper)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewDeveloper"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDeveloper_ShouldReturnUpdatedDeveloper_WhenAdmin() throws Exception {
        testDeveloper.setName("UpdatedDeveloper");

        mockMvc.perform(put("/developers/{id}", testDeveloper.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDeveloper)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedDeveloper"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDeveloper_ShouldReturnNoContent_WhenAdmin() throws Exception {
        mockMvc.perform(delete("/developers/{id}", testDeveloper.getId()))
                .andExpect(status().isNoContent());

        assertFalse(developerRepository.existsById(testDeveloper.getId()));
    }
}
