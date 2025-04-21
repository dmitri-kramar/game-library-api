package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.GameDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceUnitTests {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;
    private GameDTO testGameDTO;

    @BeforeEach
    void setUp() {
        testGame = new Game(1L, "Test Game", LocalDate.parse("2000-01-01"), "Description", null, null, null);
        testGameDTO = new GameDTO("Test Game", LocalDate.parse("2000-01-01"), "Description", null, null, null);
    }

    @Test
    void getAll_ShouldReturnGameList() {
        when(gameRepository.findAllWithRelations()).thenReturn(List.of(testGame));

        List<Game> games = gameService.getAll();

        assertThat(games).hasSize(1);
        assertThat(games.get(0).getTitle()).isEqualTo("Test Game");
        verify(gameRepository).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnGame_WhenExists() {
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGame));

        Game game = gameService.getById(1L);

        assertThat(game.getTitle()).isEqualTo("Test Game");
        verify(gameRepository).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> gameService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedGame() {
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        Game savedGame = gameService.save(testGameDTO);

        assertThat(savedGame).isNotNull();
        assertThat(savedGame.getTitle()).isEqualTo("Test Game");
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void updateById_ShouldReturnUpdatedGame() {
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGame));
        when(gameRepository.save(testGame)).thenReturn(testGame);

        Game updatedGame = gameService.updateById(1L, testGameDTO);

        assertThat(updatedGame.getTitle()).isEqualTo("Test Game");
        verify(gameRepository).save(testGame);
    }

    @Test
    void deleteById_ShouldDeleteGame_WhenExists() {
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testGame));
        doNothing().when(gameRepository).delete(testGame);

        gameService.deleteById(1L);

        verify(gameRepository).delete(testGame);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(gameRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> gameService.deleteById(1L));
    }
}
