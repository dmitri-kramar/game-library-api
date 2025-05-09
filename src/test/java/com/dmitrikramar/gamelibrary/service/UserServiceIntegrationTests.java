package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Role;
import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.repository.RoleRepository;
import com.dmitrikramar.gamelibrary.repository.UserRepository;
import com.dmitrikramar.gamelibrary.dto.PasswordDTO;
import com.dmitrikramar.gamelibrary.dto.UserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole(userRole);

        userRepository.save(testUser);
    }

    @Test
    void save() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("newUser", "newPassword");
        User savedUser = userService.save(userRequestDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newUser");
        assertThat(passwordEncoder.matches("newPassword", savedUser.getPassword())).isTrue();
    }

    @Test
    void saveUsernameAlreadyExists() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("testUser", "newPassword");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.save(userRequestDTO)
        );

        assertThat(exception.getMessage()).isEqualTo("Username already exists");
    }

    @Test
    void updatePassword() {
        PasswordDTO dto = new PasswordDTO("password123", "newPassword123");
        User updatedUser = userService.updatePassword(testUser.getId(), dto);

        assertThat(updatedUser).isNotNull();
        assertThat(passwordEncoder.matches("newPassword123", updatedUser.getPassword())).isTrue();
    }

    @Test
    void updatePasswordInvalidOldPassword() {
        PasswordDTO dto = new PasswordDTO("wrongPassword", "newPassword123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updatePassword(testUser.getId(), dto)
        );

        assertThat(exception.getMessage()).isEqualTo("Invalid old password");
    }

    @Test
    void updatePasswordSameAsOld() {
        PasswordDTO dto = new PasswordDTO("password123", "password123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updatePassword(testUser.getId(), dto)
        );

        assertThat(exception.getMessage()).isEqualTo("Invalid new password");
    }

    @Test
    void getAll() {
        List<User> users = userService.getAll();

        assertThat(users).isNotEmpty();
        assertThat(users).contains(testUser);
    }

    @Test
    void getById() {
        User foundUser = userService.getById(testUser.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(testUser.getId());
    }

    @Test
    void deleteById() {
        userService.deleteById(testUser.getId());

        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }
}
