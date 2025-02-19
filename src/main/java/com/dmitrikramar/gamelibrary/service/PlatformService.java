package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service layer for managing platforms. Provides basic CRUD operations.

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    public List<Platform> getAll() {
        return platformRepository.findAllWithRelations();
    }

    public Platform getById(Long id) {
        return platformRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Platform not found"));
    }

    @Transactional
    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }

    @Transactional
    public void deleteById(Long id) {
        platformRepository.delete(getById(id));
    }
}
