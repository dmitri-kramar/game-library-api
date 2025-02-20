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
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole(userRole);

        userRepository.save(testUser);
    }

    @Test
    void testObjectMapperUsed() {
        System.out.println("Used ObjectMapper: " + objectMapper);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[?(@.id == " + testUser.getId() + ")]").exists());
    }

    @Test
    @WithMockUser
    void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void updatePassword_ShouldReturnUpdatedUser_WhenAuthorized() throws Exception {
        PasswordTestDTO passwordTestDTO = new PasswordTestDTO("password123", "newPassword");

        mockMvc.perform(put("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordTestDTO)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();

        assertFalse(passwordEncoder.matches(passwordTestDTO.oldPassword(), updatedUser.getPassword()));
        assertTrue(passwordEncoder.matches(passwordTestDTO.newPassword(), updatedUser.getPassword()));
    }

    @Test
    @WithMockUser
    void updatePassword_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        PasswordTestDTO invalidPasswordTestDTO = new PasswordTestDTO("", "short");

        mockMvc.perform(put("/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPasswordTestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_ShouldReturnNoContent_WhenAdmin() throws Exception {
        mockMvc.perform(delete("/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.existsById(testUser.getId()));
    }
}
