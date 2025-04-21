package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service for performing security checks related to user identity and access control.
 * Specifically used in security expressions like {@code @PreAuthorize}.
 */
@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserRepository userRepository;

    /**
     * Checks whether the currently authenticated user has access to the resource with the given ID.
     *
     * @param id the ID of the user whose resource is being accessed
     * @return {@code true} if the currently authenticated user matches the given ID, otherwise {@code false}
     */
    public boolean hasAccess(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return false;
        }

        return userRepository.findByUsername(userDetails.getUsername())
                .map(user -> user.getId().equals(id))
                .orElse(false);
    }
}
