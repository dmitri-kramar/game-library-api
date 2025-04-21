package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.GenreDTO;
import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        testGenre = new Genre("Test Genre");
        genreRepository.save(testGenre);
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
    void save() {
        GenreDTO newGenre = new GenreDTO("New Genre");
        Genre savedGenre = genreService.save(newGenre);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getId()).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("New Genre");
    }

    @Test
    void updateNameById() {
        GenreDTO updated = new GenreDTO("Updated Genre");
        Genre updatedGenre = genreService.updateNameById(testGenre.getId(), updated);

        assertThat(updatedGenre).isNotNull();
        assertThat(updatedGenre.getName()).isEqualTo("Updated Genre");
    }

    @Test
    void updateNameById_ShouldThrowException_WhenNameAlreadyExists() {
        Genre existing = new Genre("Existing Genre");
        genreRepository.save(existing);

        GenreDTO duplicate = new GenreDTO("Existing Genre");

        assertThrows(IllegalArgumentException.class,
                () -> genreService.updateNameById(testGenre.getId(), duplicate));
    }

    @Test
    void deleteById() {
        genreService.deleteById(testGenre.getId());

        assertThat(genreRepository.findById(testGenre.getId())).isEmpty();
    }
}
