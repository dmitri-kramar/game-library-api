package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestTestDTO(
        @NotBlank @Size(min = 2, max = 25) String username,
        @NotBlank @Size(min = 8, max = 25) String password) {
}
