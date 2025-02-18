package com.dmitrikramar.gamelibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

// Platform entity represents a gaming platform like PC, PlayStation, or Xbox.
// A platform can be associated with multiple games.

@Entity
@Data
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany (mappedBy = "platforms")
    private Set<Game> games;
}
