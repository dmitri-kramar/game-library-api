package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.GenreDTO;
import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for managing Genre entities.
 * Handles CRUD operations and validates uniqueness of genre names.
 */
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    /**
     * Throws an exception if a genre with the given name already exists.
     *
     * @param name the name to check
     * @throws IllegalArgumentException if the name is already in use
     */
    private void throwIfExistsByName(String name){
        if (genreRepository.existsByName(name)) {
            throw new IllegalArgumentException("Genre name already exists");
        }
    }

    /**
     * Retrieves all genres with their related entities.
     *
     * @return a list of all genres
     */
    public List<Genre> getAll() {
        return genreRepository.findAllWithRelations();
    }

    /**
     * Retrieves a genre by its ID.
     *
     * @param id the ID of the genre
     * @return the genre entity
     * @throws NoSuchElementException if the genre is not found
     */
    public Genre getById(Long id) {
        return genreRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Genre not found"));
    }

    /**
     * Saves a new genre to the database.
     *
     * @param dto the genre data to save
     * @return the saved genre entity
     * @throws IllegalArgumentException if the name already exists
     */
    @Transactional
    public Genre save(GenreDTO dto) {
        throwIfExistsByName(dto.name());
        Genre newGenre = new Genre(dto.name());
        return genreRepository.save(newGenre);
    }

    /**
     * Updates the name of an existing genre.
     *
     * @param id  the ID of the genre to update
     * @param dto the new genre data
     * @return the updated genre entity
     * @throws NoSuchElementException   if the genre is not found
     * @throws IllegalArgumentException if the new name already exists
     */
    @Transactional
    public Genre updateNameById(Long id, GenreDTO dto) {
        Genre existingGenre = getById(id);
        if (!existingGenre.getName().equals(dto.name())) {
            throwIfExistsByName(dto.name());
        }

        existingGenre.setName(dto.name());
        return genreRepository.save(existingGenre);
    }

    /**
     * Deletes a genre by its ID and removes associations from related games.
     *
     * @param id the ID of the genre to delete
     * @throws NoSuchElementException if the genre is not found
     */
    @Transactional
    public void deleteById(Long id) {
        Genre existingGenre = getById(id);
        existingGenre.getGames().forEach(game -> game.getGenres().remove(existingGenre));
        genreRepository.delete(existingGenre);
    }
}
