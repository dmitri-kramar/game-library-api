package com.dmitrikramar.gamelibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Entity representing a user role in the system.
 * <p>
 * Each role has a name (such as USER or ADMIN) and can be assigned to multiple users.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    /** Unique identifier for the role (automatically generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the role.
     * <p>
     * Stored as a string representation of the {@link RoleName} enum.
     * Cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName name;

    /**
     * Users who have been assigned this role.
     * <p>
     * This is the inverse side of the one-to-many relationship with {@link User}.
     */
    @OneToMany(mappedBy = "role")
    private Set<User> users;
}