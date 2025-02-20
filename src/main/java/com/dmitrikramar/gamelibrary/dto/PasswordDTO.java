package com.dmitrikramar.gamelibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Data Transfer Object (DTO) for password change with WRITE_ONLY access for both old and new passwords.
// Old password is marked as WRITE_ONLY to prevent it from being serialized in responses.
// New password is required (non-blank) and should have a length between 8 and 25 characters.

public record PasswordDTO (
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String oldPassword,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotBlank @Size(min = 8, max = 25) String newPassword)
{}