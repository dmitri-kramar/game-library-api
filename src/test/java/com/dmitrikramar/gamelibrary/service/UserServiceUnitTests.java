package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.entity.Role;
import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.repository.UserRepository;
import com.dmitrikramar.gamelibrary.repository.RoleRepository;
import com.dmitrikramar.gamelibrary.dto.UserRequestDTO;
import com.dmitrikramar.gamelibrary.dto.PasswordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        // Creating test role and user objects before each test
        testRole = new Role(1L, RoleName.USER, null);
        testUser = new User(1L, "testUser", "encodedPassword", testRole);
    }

    @Test
    void getAll_ShouldReturnUserList() {
        // Mocking repository response
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        List<User> users = userService.getAll();
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());

        // Ensuring repository method is called once
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnUser_WhenExists() {
        // Mocking a successful find operation
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User user = userService.getById(1L);
        assertEquals("testUser", user.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        // Mocking an empty result
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Expecting an exception when the user is not found
        assertThrows(NoSuchElementException.class, () -> userService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedUser() {
        UserRequestDTO dto = new UserRequestDTO("newUser", "password");

        // Mocking repository behavior for saving a user
        when(userRepository.existsByUsername(dto.username())).thenReturn(false);
        when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(testRole));
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.save(dto);

        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updatePassword_ShouldUpdatePassword_WhenValid() {
        PasswordDTO dto = new PasswordDTO("oldPassword", "newPassword");

        // Mocking user retrieval and password validation
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(dto.oldPassword(), testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.newPassword())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = userService.updatePassword(1L, dto);

        assertNotNull(updatedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updatePassword_ShouldThrowException_WhenOldPasswordIsInvalid() {
        PasswordDTO dto = new PasswordDTO("wrongPassword", "newPassword");

        // Mocking user retrieval and password validation failure
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(dto.oldPassword(), testUser.getPassword())).thenReturn(false);

        // Expecting an exception when the old password does not match
        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(1L, dto));
    }

    @Test
    void deleteById_ShouldDeleteUser_WhenExists() {
        // Mocking a successful find operation
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Ensuring delete method doesn't throw exceptions
        doNothing().when(userRepository).delete(testUser);
        userService.deleteById(1L);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        // Mocking an empty result
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Expecting an exception when trying to delete a non-existent user
        assertThrows(NoSuchElementException.class, () -> userService.deleteById(1L));
    }
}
