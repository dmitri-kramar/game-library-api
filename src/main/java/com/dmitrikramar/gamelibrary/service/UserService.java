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

/**
 * Service class for managing user-related operations such as registration,
 * password updates, and CRUD actions. This service also ensures password validation
 * and assigns the default role to new users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Validates the old and new passwords before updating.
     *
     * @param oldPassword the current password input by the user
     * @param newPassword the new password to be set
     * @param user        the user entity from the database
     * @throws IllegalArgumentException if the old password doesn't match
     *                                  or the new password is the same as the old
     */
    private void validatePasswords(String oldPassword, String newPassword, User user) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Invalid new password");
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of users
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user entity
     * @throws NoSuchElementException if the user does not exist
     */
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    /**
     * Creates a new user with a default role and encrypted password.
     *
     * @param dto the DTO containing username and password
     * @return the saved user entity
     * @throws IllegalArgumentException if the username already exists
     * @throws IllegalStateException    if the default role is not configured
     */
    @Transactional
    public User save(UserRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * Updates the password of the user with the given ID.
     *
     * @param id  the ID of the user
     * @param dto the DTO containing old and new passwords
     * @return the updated user entity
     * @throws IllegalArgumentException if password validation fails
     */
    @Transactional
    public User updatePassword(Long id, PasswordDTO dto) {
        User user = getById(id);
        validatePasswords(dto.oldPassword(), dto.newPassword(), user);
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    @Transactional
    public void deleteById(Long id) {
        userRepository.delete(getById(id));
    }
}
