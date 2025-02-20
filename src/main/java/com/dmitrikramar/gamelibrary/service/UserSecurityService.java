package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

// Service for handling user access control based on authentication and user ID.

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserRepository userRepository;

    // Checks if the currently authenticated user has access to the resource identified by the given user ID.
    public boolean hasAccess(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If the user is not authenticated, access is denied.
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Retrieves the principal (user details) from the authentication object.
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return false;
        }

        // Checks if the user with the given username has the same ID as the requested resource.
        return userRepository.findByUsername(userDetails.getUsername())
                .map(user -> user.getId().equals(id))
                .orElse(false);
    }
}
