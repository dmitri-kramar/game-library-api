package com.dmitrikramar.gamelibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordDTO (
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String oldPassword,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotBlank @Size(min = 8, max = 25) String newPassword)
{}