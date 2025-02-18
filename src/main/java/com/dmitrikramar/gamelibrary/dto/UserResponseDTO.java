package com.dmitrikramar.gamelibrary.dto;

import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.entity.User;

public record UserResponseDTO(Long id, String username, RoleName role) {
    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole().getName());
    }
}
