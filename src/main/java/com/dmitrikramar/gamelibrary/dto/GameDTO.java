package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for creating or updating a Game entity.
 *
 * @param title        the title of the game (required, max 40 characters)
 * @param releaseDate  the release date of the game (optional)
 * @param description  a brief description of the game (optional, max 250 characters)
 * @param developerId  the ID of the associated developer (nullable)
 * @param platformIds  a set of platform IDs where the game is available (nullable)
 * @param genresIds    a set of genre IDs associated with the game (nullable)
 */
public record GameDTO(
        @NotBlank @Size(max = 40) String title,
        LocalDate releaseDate,
        @Size(max = 250) String description,
        Long developerId,
        Set<Long> platformIds,
        Set<Long> genresIds
) {}
