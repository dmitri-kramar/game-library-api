package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating or updating a Genre entity.
 *
 * @param name the name of the genre (required, 2–25 characters)
 */
public record GenreDTO(
        @NotBlank @Size(min = 2, max = 25) String name
) {}
