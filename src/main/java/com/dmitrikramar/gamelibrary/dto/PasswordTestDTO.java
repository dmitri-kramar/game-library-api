package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Data Transfer Object (DTO) for password change, with validation for the new password.
// The old password can be any string, with no specific validation.
// The new password should be non-blank, with a minimum length of 4 and a maximum of 25 characters.
// This DTO is meant to be used only during testing.

public record PasswordTestDTO(String oldPassword, @NotBlank @Size(min = 4, max = 25) String newPassword)
{}
