package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
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
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Platform platform = (Platform) o;
        return Objects.equals(id, platform.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
