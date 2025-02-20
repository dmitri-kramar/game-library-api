package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceUnitTests {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private Genre testGenre;

    @BeforeEach
    void setUp() {
        testGenre = new Genre(1L, "Test Genre", null);
    }

    @Test
    void getAll_ShouldReturnGenreList() {
        when(genreRepository.findAllWithRelations()).thenReturn(List.of(testGenre));
        List<Genre> genres = genreService.getAll();
        assertEquals(1, genres.size());
        assertEquals("Test Genre", genres.get(0).getName());
        verify(genreRepository, times(1)).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnGenre_WhenExists() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGenre));
        Genre genre = genreService.getById(1L);
        assertEquals("Test Genre", genre.getName());
        verify(genreRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> genreService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedGenre() {
        when(genreRepository.save(testGenre)).thenReturn(testGenre);
        Genre savedGenre = genreService.save(testGenre);
        assertNotNull(savedGenre);
        assertEquals("Test Genre", savedGenre.getName());
        verify(genreRepository, times(1)).save(testGenre);
    }

    @Test
    void deleteById_ShouldDeleteGenre_WhenExists() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGenre));
        doNothing().when(genreRepository).delete(testGenre);
        genreService.deleteById(1L);
        verify(genreRepository, times(1)).delete(testGenre);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(genreRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> genreService.deleteById(1L));
    }
}
