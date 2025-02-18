package com.dmitrikramar.gamelibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

// Role entity represents a role assigned to a user, such as USER or ADMIN.
// A role can be associated with multiple users.

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}