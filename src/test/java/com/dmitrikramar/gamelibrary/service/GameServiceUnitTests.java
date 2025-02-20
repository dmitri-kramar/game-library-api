package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceUnitTests {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    void setUp() {
        // Creating a test game object before each test
        testGame = new Game(1L, "Test Game", LocalDate.parse("2000-01-01"), "Description", null, null, null);
    }

    @Test
    void getAll_ShouldReturnGameList() {
        // Mocking repository response
        when(gameRepository.findAllWithRelations()).thenReturn(List.of(testGame));
        List<Game> games = gameService.getAll();
        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getTitle());

        // Ensuring repository method is called once
        verify(gameRepository, times(1)).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnGame_WhenExists() {
        // Mocking a successful find operation
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGame));
        Game game = gameService.getById(1L);
        assertEquals("Test Game", game.getTitle());
        verify(gameRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        // Mocking an empty result
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        // Expecting an exception when the game is not found
        assertThrows(NoSuchElementException.class, () -> gameService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedGame() {
        // Mocking repository save behavior
        when(gameRepository.save(testGame)).thenReturn(testGame);
        Game savedGame = gameService.save(testGame);
        assertNotNull(savedGame);
        assertEquals("Test Game", savedGame.getTitle());
        verify(gameRepository, times(1)).save(testGame);
    }

    @Test
    void deleteById_ShouldDeleteGame_WhenExists() {
        // Mocking a successful find operation
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGame));

        // Ensuring delete method doesn't throw exceptions
        doNothing().when(gameRepository).delete(testGame);
        gameService.deleteById(1L);
        verify(gameRepository, times(1)).delete(testGame);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        // Mocking an empty result
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        // Expecting an exception when trying to delete a non-existent game
        assertThrows(NoSuchElementException.class, () -> gameService.deleteById(1L));
    }
}