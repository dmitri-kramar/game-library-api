package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service layer for managing developers. Provides basic CRUD operations.

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    public List<Developer> getAll() {
        return developerRepository.findAll();
    }

    public Developer getById(Long id) {
        return developerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Developer not found"));
    }

    @Transactional
    public Developer save(Developer developer) {
        return developerRepository.save(developer);
    }

    @Transactional
    public void deleteById(Long id) {
        developerRepository.delete(getById(id));
    }
}
