package com.dmitrikramar.gamelibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

// Genre entity represents a category or type of game, such as RPG, Shooter, or Strategy.
// A genre can be associated with multiple games.

@Entity
@Data
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany (mappedBy = "genres")
    private Set<Game> games;
}
