package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service class responsible for business logic related to Platform entities.

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    // Retrieves a list of all platforms, including their related entities.
    public List<Platform> getAll() {
        return platformRepository.findAllWithRelations();
    }

    // Retrieves a platform by its ID, including related entities.
    // Throws an exception if the platform is not found.
    public Platform getById(Long id) {
        return platformRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Platform not found"));
    }

    // Saves a new or existing platform to the repository.
    @Transactional
    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }

    // Deletes a platform by its ID.
    @Transactional
    public void deleteById(Long id) {
        platformRepository.delete(getById(id));
    }
}
