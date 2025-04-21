package com.dmitrikramar.gamelibrary.dto;

import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.entity.User;

/**
 * Data Transfer Object (DTO) for returning a simplified user response.
 * Includes the user ID, username, and role name.
 *
 * @param id       the ID of the user
 * @param username the username of the user
 * @param role     the role assigned to the user
 */
public record UserResponseDTO(Long id, String username, RoleName role) {

    /**
     * Converts a {@link User} entity to a {@link UserResponseDTO}.
     *
     * @param user the user entity to convert
     * @return a new {@code UserResponseDTO} containing the user's ID, username, and role
     */
    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole().getName());
    }
}
