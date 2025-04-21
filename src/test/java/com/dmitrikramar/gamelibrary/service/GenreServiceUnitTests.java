package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.GenreDTO;
import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceUnitTests {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private Genre testGenre;
    private GenreDTO testGenreDTO;

    @BeforeEach
    void setUp() {
        testGenre = new Genre(1L, "Test Genre", new HashSet<>());
        testGenreDTO = new GenreDTO("Test Genre");
    }

    @Test
    void getAll_ShouldReturnGenreList() {
        when(genreRepository.findAllWithRelations()).thenReturn(List.of(testGenre));

        List<Genre> genres = genreService.getAll();

        assertThat(genres).hasSize(1);
        assertThat(genres.get(0).getName()).isEqualTo("Test Genre");
        verify(genreRepository).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnGenre_WhenExists() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGenre));

        Genre genre = genreService.getById(1L);

        assertThat(genre.getName()).isEqualTo("Test Genre");
        verify(genreRepository).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> genreService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedGenre() {
        when(genreRepository.save(any(Genre.class))).thenReturn(testGenre);

        Genre savedGenre = genreService.save(testGenreDTO);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("Test Genre");
        verify(genreRepository).save(any(Genre.class));
    }

    @Test
    void updateNameById_ShouldReturnUpdatedGenre() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGenre));
        when(genreRepository.save(any(Genre.class))).thenReturn(testGenre);

        GenreDTO updated = new GenreDTO("Updated Genre");
        Genre updatedGenre = genreService.updateNameById(1L, updated);

        assertThat(updatedGenre.getName()).isEqualTo("Updated Genre");
        verify(genreRepository).findByIdWithRelations(1L);
        verify(genreRepository).save(testGenre);
    }

    @Test
    void updateNameById_ShouldThrowException_WhenNameAlreadyExists() {
        GenreDTO duplicateNameDTO = new GenreDTO("Existing Genre");

        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGenre));
        when(genreRepository.existsByName("Existing Genre")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                genreService.updateNameById(1L, duplicateNameDTO));
    }

    @Test
    void deleteById_ShouldDeleteGenre_WhenExists() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGenre));
        doNothing().when(genreRepository).delete(testGenre);

        genreService.deleteById(1L);

        verify(genreRepository).delete(testGenre);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> genreService.deleteById(1L));
    }
}
