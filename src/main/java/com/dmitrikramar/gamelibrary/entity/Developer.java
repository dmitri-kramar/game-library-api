package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a game developer.
 * <p>
 * Each developer has a unique name and may be associated with multiple games.
 * This class is used to persist developer information in the database.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Developer {

    /** Unique identifier for the developer (generated automatically). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Developer's name (must be unique and not null). */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * List of games developed by this developer.
     * <p>
     * Mapped by the {@code developer} field in the {@link Game} entity.
     * Uses {@code @JsonIgnoreProperties} to prevent circular references during serialization.
     */
    @OneToMany(mappedBy = "developer")
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games = new HashSet<>();

    /**
     * Constructor to create a Developer with a specified name.
     *
     * @param name the name of the developer
     */
    public Developer(String name) {
        this.name = name;
    }
}
