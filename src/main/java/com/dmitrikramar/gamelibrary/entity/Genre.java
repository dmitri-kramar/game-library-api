package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

// Genre entity represents a category or type of game, such as RPG, Shooter, or Strategy.
// A genre can be associated with multiple games.

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany (mappedBy = "genres")
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games;

    public Genre(String name, Set<Game> games) {
        this.name = name;
        this.games = games;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
