package com.dmitrikramar.gamelibrary.dto;

import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.entity.User;

// Data Transfer Object (DTO) for returning a simplified User response, including ID, username, and role name.

public record UserResponseDTO(Long id, String username, RoleName role) {

    // Static method to map a User entity to a UserResponseDTO
    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole().getName());
    }
}
