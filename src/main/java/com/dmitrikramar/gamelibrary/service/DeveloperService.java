package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service class responsible for business logic related to Developer entities.

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    // Retrieves a list of all developers, including their related entities.
    public List<Developer> getAll() {
        return developerRepository.findAllWithRelations();
    }

    // Retrieves a developer by its ID, including related entities.
    // Throws an exception if the developer is not found.
    public Developer getById(Long id) {
        return developerRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Developer not found"));
    }

    // Saves a new or existing developer to the repository.
    @Transactional
    public Developer save(Developer developer) {
        return developerRepository.save(developer);
    }

    // Deletes a developer by its ID.
    @Transactional
    public void deleteById(Long id) {
        developerRepository.delete(getById(id));
    }
}
