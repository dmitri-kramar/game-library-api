package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.PasswordDTO;
import com.dmitrikramar.gamelibrary.dto.UserRequestDTO;
import com.dmitrikramar.gamelibrary.entity.Role;
import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.repository.RoleRepository;
import com.dmitrikramar.gamelibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service class for managing user-related operations
// such as registration, retrieval, updating passwords, and deletion.

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Validates old and new passwords for correctness.
    private void validatePasswords(String oldPassword, String newPassword, User user) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Invalid new password");
        }
    }

    // Retrieves all users from the repository.
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Retrieves a user by ID. Throws exception if not found.
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    // Registers a new user if the username is not taken and returns the saved user.
    @Transactional
    public User save(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Fetches the default USER role.
        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));

        // Creates a new user with encrypted password.
        User user = new User();
        user.setUsername(userRequestDTO.username());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setRole(role);

        return userRepository.save(user);
    }

    // Updates the user's password after validating the old one.
    @Transactional
    public User updatePassword(Long id, PasswordDTO dto) {
        User user = getById(id);
        validatePasswords(dto.oldPassword(), dto.newPassword(), user);
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        return userRepository.save(user);
    }

    // Deletes a user by ID.
    @Transactional
    public void deleteById(Long id) {
        userRepository.delete(getById(id));
    }
}
