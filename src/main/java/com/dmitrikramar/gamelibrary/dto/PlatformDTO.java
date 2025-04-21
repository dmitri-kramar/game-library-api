package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating or updating a Platform entity
 *
 * @param name the name of the platform (required, 2–25 characters)
 */
public record PlatformDTO(
        @NotBlank @Size(min = 2, max = 25) String name
) {}
