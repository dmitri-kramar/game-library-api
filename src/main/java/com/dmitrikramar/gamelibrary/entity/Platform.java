package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing a gaming platform.
 * <p>
 * Each platform has a unique name and can be associated with multiple games.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Platform {

    /** Unique identifier for the platform (automatically generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the platform (must be unique and not null). */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Games available on this platform.
     * <p>
     * This is the inverse side of the many-to-many relationship with {@link Game}.
     * {@code @JsonIgnoreProperties} is used to prevent infinite recursion during serialization.
     */
    @ManyToMany (mappedBy = "platforms")
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games = new HashSet<>();

    /**
     * Constructor to create a platform with a given name.
     *
     * @param name the name of the platform
     */
    public Platform(String name) {
        this.name = name;
    }

    /**
     * Overrides equals to compare platforms based on their ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Platform platform = (Platform) o;
        return Objects.equals(id, platform.id);
    }

    /**
     * Hash code based on the platform's ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
