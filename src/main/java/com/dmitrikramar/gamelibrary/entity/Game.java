package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

// Game entity represents a video game with details like title, release date, rating, and description.
// It is associated with a developer, platforms, genres, and reviews.

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = false)
    @JsonIgnoreProperties("games")
    private Developer developer;

    @ManyToMany
    @JoinTable(
            name = "game_platform",
            joinColumns = @JoinColumn(name = "game_id", nullable = false),
            inverseJoinColumns = @JoinColumn (name = "platform_id", nullable = false)
    )
    @JsonIgnoreProperties("games")
    private Set<Platform> platforms;

    @ManyToMany
    @JoinTable(
            name = "game_genre",
            joinColumns = @JoinColumn(name = "game_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "genre_id", nullable = false)
    )
    @JsonIgnoreProperties("games")
    private Set<Genre> genres;

    public Game(String title, LocalDate releaseDate, String description, Developer developer,
                Set<Platform> platforms, Set<Genre> genres) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
        this.developer = developer;
        this.platforms = platforms;
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
