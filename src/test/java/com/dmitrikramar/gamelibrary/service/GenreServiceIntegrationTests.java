package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class GenreServiceIntegrationTests {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    private Genre testGenre;

    @BeforeEach
    void setUp() {
        // Creates and saves a test genre before each test
        testGenre = new Genre("TestGenre", null);
        genreRepository.save(testGenre);
    }

    @Test
    void save() {
        // Verifies that a new genre is saved correctly with a generated ID
        Genre newGenre = new Genre("NewGenre", null);
        Genre savedGenre = genreService.save(newGenre);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getId()).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("NewGenre");
    }

    @Test
    void getAll() {
        // Verifies that all genres are retrieved, including the test genre
        List<Genre> genres = genreService.getAll();
        assertThat(genres).isNotEmpty();
        assertThat(genres).contains(testGenre);
    }

    @Test
    void getById() {
        // Verifies that the genre is retrieved correctly by ID
        Genre foundGenre = genreService.getById(testGenre.getId());

        assertThat(foundGenre).isNotNull();
        assertThat(foundGenre.getId()).isEqualTo(testGenre.getId());
    }

    @Test
    void deleteById() {
        // Verifies that a genre is deleted correctly and no longer exists in the repository
        genreService.deleteById(testGenre.getId());
        assertThat(genreRepository.findById(testGenre.getId())).isEmpty();
    }
}
