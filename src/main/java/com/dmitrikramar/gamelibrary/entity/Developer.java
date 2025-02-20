package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// Developer entity represents a game development company with a name and a country.
// Each developer is associated with multiple games they have created.

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "developer")
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games;

    public Developer(String name, Set<Game> games) {
        this.name = name;
        this.games = games;
    }
}
