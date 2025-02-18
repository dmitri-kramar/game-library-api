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

// Service layer for managing users. Provides basic CRUD operations.

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private void validatePasswords(String oldPassword, String newPassword, User user) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Invalid new password");
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public User save(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));

        User user = new User();
        user.setUsername(userRequestDTO.username());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Transactional
    public User updatePassword(Long id, PasswordDTO dto) {
        User user = getById(id);
        validatePasswords(dto.oldPassword(), dto.newPassword(), user);
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.delete(getById(id));
    }
}
