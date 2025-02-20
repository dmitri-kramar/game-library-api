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
        testRole = new Role(1L, RoleName.USER, null);
        testUser = new User(1L, "testUser", "encodedPassword", testRole);
    }

    @Test
    void getAll_ShouldReturnUserList() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        List<User> users = userService.getAll();
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User user = userService.getById(1L);
        assertEquals("testUser", user.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedUser() {
        UserRequestDTO dto = new UserRequestDTO("newUser", "password");
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
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(dto.oldPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(1L, dto));
    }

    @Test
    void deleteById_ShouldDeleteUser_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);
        userService.deleteById(1L);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.deleteById(1L));
    }
}
