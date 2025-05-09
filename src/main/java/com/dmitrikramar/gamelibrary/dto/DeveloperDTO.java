package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating or updating a Developer.
 *
 * @param name the name of the developer (2–25 characters, must not be blank)
 */
public record DeveloperDTO(
        @NotBlank @Size(min = 2, max = 25) String name
) {}
