package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

// Platform entity represents a gaming platform like PC, PlayStation, or Xbox.
// A platform can be associated with multiple games.

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany (mappedBy = "platforms")
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games;

    public Platform(String name, Set<Game> games) {
        this.name = name;
        this.games = games;
    }

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
