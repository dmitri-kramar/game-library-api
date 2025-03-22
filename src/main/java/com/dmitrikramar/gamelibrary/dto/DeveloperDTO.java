package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeveloperDTO(@NotBlank @Size(min = 2, max = 25) String name) {
}
