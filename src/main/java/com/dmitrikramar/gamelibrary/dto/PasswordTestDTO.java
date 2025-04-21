package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used for testing password changes. The old password has no validation;
 * the new password must be 4–25 characters and non-blank.
 *
 * @param oldPassword the current password (no validation)
 * @param newPassword the new password (required, 4–25 characters)
 */
public record PasswordTestDTO(
        String oldPassword,
        @NotBlank @Size(min = 4, max = 25) String newPassword
) {}
