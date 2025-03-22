package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.PasswordTestDTO;
import com.dmitrikramar.gamelibrary.entity.*;
import com.dmitrikramar.gamelibrary.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Initializes a test user with the role 'USER' and saves it to the repository
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole(userRole);

        userRepository.save(testUser);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Sends a GET request to fetch all users and expects a non-empty list
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testUser.getId() + ")]").exists());
    }

    @Test
    @WithMockUser
    void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        // Sends a GET request to fetch a specific user and expects the user's username to match
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void updatePassword_ShouldReturnUpdatedUser_WhenAuthorized() throws Exception {
        PasswordTestDTO passwordTestDTO = new PasswordTestDTO("password123", "newPassword");

        // Sends a PUT request to updateName the user's password and expects it to be updated correctly
        mockMvc.perform(put("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordTestDTO)))
                .andExpect(status().isOk());

        // Verifies the password was updated in the repository
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();

        // Verifies the old password no longer matches, and the new password matches
        assertFalse(passwordEncoder.matches(passwordTestDTO.oldPassword(), updatedUser.getPassword()));
        assertTrue(passwordEncoder.matches(passwordTestDTO.newPassword(), updatedUser.getPassword()));
    }

    @Test
    @WithMockUser
    void updatePassword_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        PasswordTestDTO invalidPasswordTestDTO = new PasswordTestDTO("", "short");

        // Sends a PUT request to updateName the password with invalid data and expects a bad request response
        mockMvc.perform(put("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPasswordTestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_ShouldReturnNoContent_WhenAdmin() throws Exception {
        // Sends a DELETE request to remove the user and expects a no content response
        mockMvc.perform(delete("/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());

        // Verifies the user was actually deleted from the repository
        assertFalse(userRepository.existsById(testUser.getId()));
    }
}
