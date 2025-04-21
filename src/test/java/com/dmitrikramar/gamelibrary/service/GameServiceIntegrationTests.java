package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.GameDTO;
import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class GameServiceIntegrationTests {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    private Game testGame;

    @BeforeEach
    void setUp() {
        testGame = new Game("Test Game", null, null, null, null, null);
        gameRepository.save(testGame);
    }

    @Test
    void getAll() {
        List<Game> games = gameService.getAll();

        assertThat(games).isNotEmpty();
        assertThat(games.stream().anyMatch(g -> g.getId().equals(testGame.getId()))).isTrue();
    }

    @Test
    void getById() {
        Game foundGame = gameService.getById(testGame.getId());

        assertThat(foundGame).isNotNull();
        assertThat(foundGame.getId()).isEqualTo(testGame.getId());
    }

    @Test
    void save() {
        GameDTO newGame = new GameDTO("New Game", null, null, null, null, null);
        Game savedGame = gameService.save(newGame);

        assertThat(savedGame).isNotNull();
        assertThat(savedGame.getId()).isNotNull();
        assertThat(savedGame.getTitle()).isEqualTo("New Game");
    }

    @Test
    void updateById() {
        GameDTO newGame = new GameDTO("Updated Game", null, null, null, null, null);
        Game updatedGame = gameService.updateById(testGame.getId(), newGame);

        assertThat(updatedGame).isNotNull();
        assertThat(updatedGame.getTitle()).isEqualTo("Updated Game");
    }

    @Test
    void deleteById() {
        gameService.deleteById(testGame.getId());

        assertThat(gameRepository.findById(testGame.getId())).isEmpty();
    }
}

