package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSecurityServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserSecurityService userSecurityService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Initializing test user and setting security context
        testUser = new User(1L, "testUser", "encodedPassword", null);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void hasAccess_ShouldReturnTrue_WhenUserExistsAndMatches() {
        // Mocking authenticated user with matching username
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        assertTrue(userSecurityService.hasAccess(1L));
    }

    @Test
    void hasAccess_ShouldReturnFalse_WhenAuthenticationIsNull() {
        // If authentication is null, access should be denied
        when(securityContext.getAuthentication()).thenReturn(null);
        assertFalse(userSecurityService.hasAccess(1L));
    }

    @Test
    void hasAccess_ShouldReturnFalse_WhenPrincipalIsNotUserDetails() {
        // If principal is not an instance of UserDetails, access should be denied
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(new Object());

        assertFalse(userSecurityService.hasAccess(1L));
    }

    @Test
    void hasAccess_ShouldReturnFalse_WhenUserNotFound() {
        // If the user is not found in the repository, access should be denied
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertFalse(userSecurityService.hasAccess(1L));
    }
}
