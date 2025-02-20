package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.User;
import com.dmitrikramar.gamelibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation of Spring Security's UserDetailsService interface for loading user details from the repository.

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Loads user details by username for authentication purposes.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Retrieves the user from the repository based on the username.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Returns a Spring Security User object with roles and password.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name()))
        );
    }
}
