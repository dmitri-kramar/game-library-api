package com.dmitrikramar.gamelibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing an application user.
 * Each user has a unique username, a password, and is associated with a role.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /** Unique identifier for the user (automatically generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique username used for authentication. */
    @Column(unique = true, nullable = false)
    private String username;

    /** Hashed password for authentication. */
    @Column(nullable = false)
    private String password;

    /** Role assigned to the user (e.g., USER or ADMIN). */
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Constructs a new User with the specified username, password, and role.
     *
     * @param username the user's username
     * @param password the user's hashed password
     * @param role     the role assigned to the user
     */
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
