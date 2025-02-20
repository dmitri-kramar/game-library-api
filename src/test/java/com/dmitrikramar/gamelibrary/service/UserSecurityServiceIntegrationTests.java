package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Role;
import com.dmitrikramar.gamelibrary.entity.RoleName;
import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.repository.RoleRepository;
import com.dmitrikramar.gamelibrary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UserSecurityServiceIntegrationTests {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        // Retrieve the role 'USER' from the database
        testRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        // Create and save a test user
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password123");
        testUser.setRole(testRole);
        userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "testUser")
    void hasAccess_validUser() {
        // Test if the current authenticated user has access to the resource
        boolean hasAccess = userSecurityService.hasAccess(testUser.getId());
        assertThat(hasAccess).isTrue();
    }

    @Test
    @WithMockUser(username = "testUser")
    void hasAccess_invalidUser() {
        // Create another user and save to repository
        User otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("password123");
        otherUser.setRole(testRole);
        userRepository.save(otherUser);

        // Test if the authenticated user does not have access to another user's resource
        boolean hasAccess = userSecurityService.hasAccess(otherUser.getId());
        assertThat(hasAccess).isFalse();
    }

    @Test
    void hasAccess_noAuthentication() {
        // Mock the SecurityContext and Authentication objects
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Test if no authentication results in lack of access
        boolean hasAccess = userSecurityService.hasAccess(testUser.getId());
        assertThat(hasAccess).isFalse();
    }

    @Test
    void hasAccess_notAuthenticated() {
        // Mock authentication to simulate an unauthenticated user
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Test if a non-authenticated user cannot access resources
        boolean hasAccess = userSecurityService.hasAccess(testUser.getId());
        assertThat(hasAccess).isFalse();
    }

    @Test
    void hasAccess_userDetailsNotInstanceOfUser() {
        // Mock authentication where the principal is not an instance of User
        Authentication authentication = mock(Authentication.class);
        Object principal = new Object();
        when(authentication.getPrincipal()).thenReturn(principal);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Test if the system properly denies access when the principal is not a User
        boolean hasAccess = userSecurityService.hasAccess(testUser.getId());
        assertThat(hasAccess).isFalse();
    }
}
