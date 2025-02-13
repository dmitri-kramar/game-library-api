package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service layer for managing platforms. Provides basic CRUD operations.

@Service
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    public Optional<Platform> getById(Long id) {
        return platformRepository.findById(id);
    }

    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }

    public void deleteById(Long id) {
        platformRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return platformRepository.existsById(id);
    }
}
