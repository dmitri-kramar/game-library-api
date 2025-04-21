package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.GameDTO;
import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing Game entities.
 * Handles CRUD operations and maps DTOs to entities with related associations.
 */
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final DeveloperService developerService;
    private final PlatformService platformService;
    private final GenreService genreService;

    /**
     * Maps a GameDTO to a Game entity by resolving associated entities.
     *
     * @param dto  the source data
     * @param game the target game entity to update
     */
    private void mapDtoToGame(GameDTO dto, Game game) {
        Developer developer = dto.developerId() == null
                ? null
                : developerService.getById(dto.developerId());

        Set<Platform> platforms = dto.platformIds() == null
                ? null
                : dto.platformIds().stream()
                .map(platformService::getById)
                .collect(Collectors.toSet());

        Set<Genre> genres = dto.genresIds() == null
                ? null
                : dto.genresIds().stream()
                .map(genreService::getById)
                .collect(Collectors.toSet());

        game.setTitle(dto.title());
        game.setReleaseDate(dto.releaseDate());
        game.setDescription(dto.description());
        game.setDeveloper(developer);
        game.setPlatforms(platforms);
        game.setGenres(genres);
    }

    /**
     * Retrieves all games with their related entities.
     *
     * @return a list of all games
     */
    public List<Game> getAll() {
        return gameRepository.findAllWithRelations();
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param id the ID of the game
     * @return the game entity
     * @throws NoSuchElementException if the game is not found
     */
    public Game getById(Long id) {
        return gameRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));
    }

    /**
     * Saves a new game to the database.
     *
     * @param dto the game data to save
     * @return the saved game entity
     */
    @Transactional
    public Game save(GameDTO dto) {
        Game newGame = new Game();
        mapDtoToGame(dto, newGame);
        return gameRepository.save(newGame);
    }

    /**
     * Updates an existing game with new data.
     *
     * @param id  the ID of the game to update
     * @param dto the new game data
     * @return the updated game entity
     * @throws NoSuchElementException if the game is not found
     */
    @Transactional
    public Game updateById(Long id, GameDTO dto) {
        Game existingGame = getById(id);
        mapDtoToGame(dto, existingGame);
        return gameRepository.save(existingGame);
    }

    /**
     * Deletes a game by its ID.
     *
     * @param id the ID of the game to delete
     * @throws NoSuchElementException if the game is not found
     */
    @Transactional
    public void deleteById(Long id) {
        gameRepository.delete(getById(id));
    }
}
