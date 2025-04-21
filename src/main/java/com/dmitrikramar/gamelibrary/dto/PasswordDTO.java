package com.dmitrikramar.gamelibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating a user's password. Both fields are write-only and not exposed in responses.
 *
 * @param oldPassword the user's current password (write-only)
 * @param newPassword the new password (required, 4–25 characters, write-only)
 */
public record PasswordDTO (
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String oldPassword,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotBlank @Size(min = 4, max = 25) String newPassword
) {}