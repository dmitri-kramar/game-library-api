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
        testGenre = new Genre("TestGenre", null);
        genreRepository.save(testGenre);
    }

    @Test
    void save() {
        Genre newGenre = new Genre("NewGenre", null);
        Genre savedGenre = genreService.save(newGenre);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getId()).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("NewGenre");
    }

    @Test
    void getAll() {
        List<Genre> genres = genreService.getAll();
        assertThat(genres).isNotEmpty();
        assertThat(genres).contains(testGenre);
    }

    @Test
    void getById() {
        Genre foundGenre = genreService.getById(testGenre.getId());

        assertThat(foundGenre).isNotNull();
        assertThat(foundGenre.getId()).isEqualTo(testGenre.getId());
    }

    @Test
    void deleteById() {
        genreService.deleteById(testGenre.getId());
        assertThat(genreRepository.findById(testGenre.getId())).isEmpty();
    }
}
