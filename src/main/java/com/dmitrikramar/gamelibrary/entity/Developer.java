package com.dmitrikramar.gamelibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

// Developer entity represents a game development company with a name and a country.
// Each developer is associated with multiple games they have created.

@Entity
@Data
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "developer")
    @JsonIgnoreProperties({"developer", "platforms", "genres", "releaseDate", "description"})
    private Set<Game> games;
}
