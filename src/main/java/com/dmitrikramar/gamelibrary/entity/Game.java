package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing a game in the system.
 * <p>
 * Each game has a title, optional release date and description, and can be associated
 * with a single developer, multiple platforms, and multiple genres.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    /** Unique identifier for the game (generated automatically). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Title of the game (required). */
    @Column(nullable = false)
    private String title;

    /** Optional release date of the game. */
    @Column
    private LocalDate releaseDate;

    /** Optional description of the game. */
    @Column
    private String description;

    /**
     * The developer that created the game.
     * <p>
     * Many games can be associated with one developer.
     * {@code @JsonIgnoreProperties} is used to avoid circular references in serialization.
     */
    @ManyToOne
    @JoinColumn(name = "developer_id")
    @JsonIgnoreProperties("games")
    private Developer developer;

    /**
     * Platforms on which the game is available.
     * <p>
     * Many-to-many relationship with {@link Platform}.
     * Data is stored in the {@code game_platform} join table.
     */
    @ManyToMany
    @JoinTable(
            name = "game_platform",
            joinColumns = @JoinColumn(name = "game_id", nullable = false),
            inverseJoinColumns = @JoinColumn (name = "platform_id", nullable = false)
    )
    @JsonIgnoreProperties("games")
    private Set<Platform> platforms = new HashSet<>();

    /**
     * Genres associated with the game.
     * <p>
     * Many-to-many relationship with {@link Genre}.
     * Data is stored in the {@code game_genre} join table.
     */
    @ManyToMany
    @JoinTable(
            name = "game_genre",
            joinColumns = @JoinColumn(name = "game_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "genre_id", nullable = false)
    )
    @JsonIgnoreProperties("games")
    private Set<Genre> genres = new HashSet<>();

    /**
     * Constructor for creating a game with all fields.
     *
     * @param title       the title of the game
     * @param releaseDate the release date
     * @param description the description
     * @param developer   the developer
     * @param platforms   the platforms
     * @param genres      the genres
     */
    public Game(String title, LocalDate releaseDate, String description, Developer developer,
                Set<Platform> platforms, Set<Genre> genres) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
        this.developer = developer;
        this.platforms = platforms;
        this.genres = genres;
    }

    /**
     * Overrides equals to compare games based on their ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    /**
     * Hash code based on the game's ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
