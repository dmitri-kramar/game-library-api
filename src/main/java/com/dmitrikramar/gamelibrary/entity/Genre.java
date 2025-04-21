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
 * Entity representing a genre in the system.
 * <p>
 * Each genre has a unique name and can be associated with multiple games.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    /** Unique identifier for the genre (automatically generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the genre (must be unique and not null). */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Games associated with this genre.
     * <p>
     * This is the inverse side of the many-to-many relationship with {@link Game}.
     * {@code @JsonIgnoreProperties} is used to prevent circular references during serialization.
     */
    @ManyToMany (mappedBy = "genres")
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games = new HashSet<>();

    /**
     * Constructor to create a genre with a given name.
     *
     * @param name the name of the genre
     */
    public Genre(String name) {
        this.name = name;
    }

    /**
     * Overrides equals to compare genres based on their ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id);
    }

    /**
     * Hash code based on the genre's ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
