package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for receiving user data in requests, used specifically in tests.
 * Contains validation constraints for both username and password.
 *
 * @param username the username (required, 2–25 characters)
 * @param password the password (required, 4–25 characters)
 */
public record UserRequestTestDTO(
        @NotBlank @Size(min = 2, max = 25) String username,
        @NotBlank @Size(min = 4, max = 25) String password)
{}
