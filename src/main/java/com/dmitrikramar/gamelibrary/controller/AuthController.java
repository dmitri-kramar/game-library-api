package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.UserRequestDTO;
import com.dmitrikramar.gamelibrary.dto.UserResponseDTO;
import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user authentication and registration.
 * Provides endpoints for logging in and registering new users.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user with the provided username and password.
     * Accessible only to non-authenticated users.
     *
     * @param dto the DTO containing user registration details
     * @return the registered user data with HTTP status 201
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        User savedUser = userService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromUser(savedUser));
    }

    /**
     * Authenticates the user with the provided credentials.
     * Accessible only to non-authenticated users.
     *
     * @param dto the DTO containing login credentials
     * @return a success message if authentication succeeds
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("Logged in successfully");
    }
}
