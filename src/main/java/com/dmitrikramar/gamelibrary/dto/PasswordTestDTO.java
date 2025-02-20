package com.dmitrikramar.gamelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordTestDTO(String oldPassword, @NotBlank @Size(min = 8, max = 25) String newPassword)
{}
