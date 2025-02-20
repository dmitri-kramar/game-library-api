package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Data Transfer Object (DTO) for receiving user data in requests, with validation constraints.
// The username should be non-blank, with a minimum length of 2 and a maximum of 25 characters.
// The password should be non-blank, with a minimum length of 8 and a maximum of 25 characters.
// This DTO is meant to be used only during testing.

public record UserRequestTestDTO(
        @NotBlank @Size(min = 2, max = 25) String username,
        @NotBlank @Size(min = 8, max = 25) String password) {
}
