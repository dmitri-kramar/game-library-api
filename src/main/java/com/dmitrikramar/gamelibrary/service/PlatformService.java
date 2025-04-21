package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.PlatformDTO;
import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for managing Platform entities.
 * Handles CRUD operations and validates uniqueness of platform names.
 */
@Service
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    /**
     * Throws an exception if a platform with the given name already exists.
     *
     * @param name the name to check
     * @throws IllegalArgumentException if the name is already in use
     */
    private void throwIfExistsByName(String name){
        if (platformRepository.existsByName(name)) {
            throw new IllegalArgumentException("Platform name already exists");
        }
    }

    /**
     * Retrieves all platforms with their related entities.
     *
     * @return a list of all platforms
     */
    public List<Platform> getAll() {
        return platformRepository.findAllWithRelations();
    }

    /**
     * Retrieves a platform by its ID.
     *
     * @param id the ID of the platform
     * @return the platform entity
     * @throws NoSuchElementException if the platform is not found
     */
    public Platform getById(Long id) {
        return platformRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Platform not found"));
    }

    /**
     * Saves a new platform to the database.
     *
     * @param dto the platform data to save
     * @return the saved platform entity
     * @throws IllegalArgumentException if the name already exists
     */
    @Transactional
    public Platform save(PlatformDTO dto) {
        throwIfExistsByName(dto.name());
        Platform newPlatform = new Platform(dto.name());
        return platformRepository.save(newPlatform);
    }

    /**
     * Updates the name of an existing platform.
     *
     * @param id  the ID of the platform to update
     * @param dto the new platform data
     * @return the updated platform entity
     * @throws NoSuchElementException   if the platform is not found
     * @throws IllegalArgumentException if the new name already exists
     */
    @Transactional
    public Platform updateNameById(Long id, PlatformDTO dto) {
        Platform existingPlatform = getById(id);
        if (!existingPlatform.getName().equals(dto.name())) {
            throwIfExistsByName(dto.name());
        }

        existingPlatform.setName(dto.name());
        return platformRepository.save(existingPlatform);
    }

    /**
     * Deletes a platform by its ID and removes associations from related games.
     *
     * @param id the ID of the platform to delete
     * @throws NoSuchElementException if the platform is not found
     */
    @Transactional
    public void deleteById(Long id) {
        Platform existingPlatform = getById(id);
        existingPlatform.getGames().forEach(game -> game.getPlatforms().remove(existingPlatform));
        platformRepository.delete(existingPlatform);
    }
}
