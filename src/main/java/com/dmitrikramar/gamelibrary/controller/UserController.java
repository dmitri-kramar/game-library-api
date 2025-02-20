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

// Controller for handling user-related API requests.

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Endpoint to get all users, accessible only to ADMIN role
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserResponseDTO> response = users.stream()
                .map(UserResponseDTO::fromUser)
                .toList();
        return ResponseEntity.ok(response);
    }

    // Endpoint to get a user by ID, accessible to authenticated users
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponseDTO.fromUser(userService.getById(id)));
    }

    // Endpoint to update a user's password, accessible only by the user himself
    @PutMapping("/{id}")
    @PreAuthorize("@userSecurityService.hasAccess(#id)")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordDTO dto) {
        User updatedUser = userService.updatePassword(id, dto);
        return ResponseEntity.ok(UserResponseDTO.fromUser(updatedUser));
    }

    // Endpoint to delete a user by ID, accessible by ADMIN or the user himself
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurityService.hasAccess(#id)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
