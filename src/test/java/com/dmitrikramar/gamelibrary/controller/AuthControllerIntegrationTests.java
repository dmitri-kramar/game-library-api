package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.UserRequestDTO;
import com.dmitrikramar.gamelibrary.dto.UserRequestTestDTO;
import com.dmitrikramar.gamelibrary.entity.*;
import com.dmitrikramar.gamelibrary.repository.*;
import com.dmitrikramar.gamelibrary.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestTestDTO userRequestTestDTO;

    @BeforeEach
    void setUp() {
        // Initializes the test data before each test
        userRequestTestDTO = new UserRequestTestDTO("testUser", "password123");
    }

    // Converts UserRequestTestDTO to UserRequestDTO for the service layer
    public UserRequestDTO convertToDTO(UserRequestTestDTO userRequestTestDTO) {
        return new UserRequestDTO(userRequestTestDTO.username(), userRequestTestDTO.password());
    }

    @Test
    void registerUser_ShouldReturnCreatedUser_WhenValidRequest() throws Exception {
        // Sends a POST request to /register and expects a CREATED response

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestTestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(userRequestTestDTO.username()));

        // Verifies the user was saved in the database
        User savedUser = userRepository.findByUsername(userRequestTestDTO.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertNotNull(savedUser);
        assertEquals(userRequestTestDTO.username(), savedUser.getUsername());
    }

    @Test
    void loginUser_ShouldReturnSuccessMessage_WhenValidCredentials() throws Exception {
        userService.save(convertToDTO(userRequestTestDTO));

        // Sends a POST request to /login with correct credentials and expects a success message
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestTestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged in successfully"));
    }

    @Test
    void loginUser_ShouldReturnUnauthorized_WhenInvalidCredentials() throws Exception {
        userRequestTestDTO = new UserRequestTestDTO("testUser", "wrongPassword");

        // Sends a POST request to /login with incorrect credentials and expects an unauthorized response
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestTestDTO)))
                .andExpect(status().isUnauthorized());
    }
}
