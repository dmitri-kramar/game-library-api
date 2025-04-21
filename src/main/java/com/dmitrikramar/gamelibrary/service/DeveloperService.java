package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.DeveloperDTO;
import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for managing Developer entities.
 * Provides CRUD operations and ensures name uniqueness.
 */
@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    /**
     * Throws an exception if a developer with the given name already exists.
     *
     * @param name the name to check
     * @throws IllegalArgumentException if the name already exists
     */
    private void throwIfExistsByName(String name){
        if (developerRepository.existsByName(name)) {
            throw new IllegalArgumentException("Developer name already exists");
        }
    }

    /**
     * Retrieves all developers with their related games.
     *
     * @return a list of all developers
     */
    public List<Developer> getAll() {
        return developerRepository.findAllWithRelations();
    }

    /**
     * Retrieves a developer by its ID.
     *
     * @param id the ID of the developer
     * @return the developer entity
     * @throws NoSuchElementException if the developer is not found
     */
    public Developer getById(Long id) {
        return developerRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Developer not found"));
    }

    /**
     * Saves a new developer to the database.
     *
     * @param dto the developer data to save
     * @return the saved developer entity
     * @throws IllegalArgumentException if a developer with the same name already exists
     */
    @Transactional
    public Developer save(DeveloperDTO dto) {
        throwIfExistsByName(dto.name());
        Developer newDeveloper = new Developer(dto.name());
        return developerRepository.save(newDeveloper);
    }

    /**
     * Updates the name of an existing developer.
     *
     * @param id the ID of the developer to update
     * @param dto the new developer data
     * @return the updated developer entity
     * @throws NoSuchElementException if the developer is not found
     * @throws IllegalArgumentException if the new name already exists
     */
    @Transactional
    public Developer updateNameById(Long id, DeveloperDTO dto) {
        Developer existingDeveloper = getById(id);
        if (!existingDeveloper.getName().equals(dto.name())) {
            throwIfExistsByName(dto.name());
        }

        existingDeveloper.setName(dto.name());
        return developerRepository.save(existingDeveloper);
    }

    /**
     * Deletes a developer by ID and unlinks it from all related games.
     *
     * @param id the ID of the developer to delete
     * @throws NoSuchElementException if the developer is not found
     */
    @Transactional
    public void deleteById(Long id) {
        Developer existingDeveloper = getById(id);
        existingDeveloper.getGames().forEach(game -> game.setDeveloper(null));
        developerRepository.delete(existingDeveloper);
    }
}
