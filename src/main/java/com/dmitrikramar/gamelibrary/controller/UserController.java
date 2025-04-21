package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.PasswordDTO;
import com.dmitrikramar.gamelibrary.dto.UserResponseDTO;
import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing users.
 * Provides endpoints to retrieve, update, and delete user accounts.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a list of all users.
     * Accessible only to users with ADMIN role.
     *
     * @return list of users with HTTP 200 status
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserResponseDTO> response = users.stream()
                .map(UserResponseDTO::fromUser)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a user by their ID.
     * Accessible to any authenticated user.
     *
     * @param id the ID of the user
     * @return user data with HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponseDTO.fromUser(userService.getById(id)));
    }

    /**
     * Updates a user's password.
     * Accessible only to the user themselves.
     *
     * @param id  the ID of the user
     * @param dto the old and new password
     * @return updated user data with HTTP 200 status
     */
    @PutMapping("/{id}")
    @PreAuthorize("@userSecurityService.hasAccess(#id)")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordDTO dto) {
        User updatedUser = userService.updatePassword(id, dto);
        return ResponseEntity.ok(UserResponseDTO.fromUser(updatedUser));
    }

    /**
     * Deletes a user by ID.
     * Accessible by ADMIN or the user themselves.
     *
     * @param id the ID of the user to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurityService.hasAccess(#id)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
