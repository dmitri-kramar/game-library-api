package com.dmitrikramar.gamelibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Data Transfer Object (DTO) for user request data, with validation and specific access rules for password.
// The username should be non-blank, with a minimum length of 2 and a maximum of 25 characters.
// The password should be non-blank, with a minimum length of 8 and a maximum of 25 characters.
// The password will only be written during deserialization, not included in the response (write-only).

public record UserRequestDTO(
        @NotBlank @Size(min = 2, max = 25) String username,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank @Size(min = 8, max = 25) String password)
{}
