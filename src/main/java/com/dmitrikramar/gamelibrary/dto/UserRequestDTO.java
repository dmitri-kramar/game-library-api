package com.dmitrikramar.gamelibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for user registration and login requests.
 * Contains validation constraints and ensures password is write-only.
 *
 * @param username the username (required, 2–25 characters)
 * @param password the password (required, 4–25 characters; write-only)
 */
public record UserRequestDTO(
        @NotBlank @Size(min = 2, max = 25) String username,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank @Size(min = 4, max = 25) String password
) {}
