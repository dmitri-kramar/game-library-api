package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Role;
import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.repository.RoleRepository;
import com.dmitrikramar.gamelibrary.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDetailsServiceImplIntegrationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_Success() {
        // Mocking role retrieval to return a user role
        Role userRole = new Role();
        userRole.setName(RoleName.USER);

        // Mocking role repository to return the user role
        Mockito.when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(userRole));

        // Creating a mock user
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(userRole);

        // Mocking user repository to return the mock user
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Calling the method under test
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Verifying the user details returned are correct
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String username = "testUser";

        // Mocking the user repository to return an empty Optional (user not found)
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Verifying that a UsernameNotFoundException is thrown when the user is not found
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}

