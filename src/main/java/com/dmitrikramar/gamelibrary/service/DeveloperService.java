package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.DeveloperDTO;
import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.entity.Game;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/*
Service class responsible for business logic related to Developer entities.
*/

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    /*
    Retrieves a list of all developers, including their related entities.
    */

    public List<Developer> getAll() {
        return developerRepository.findAllWithRelations();
    }

    /*
    Retrieves a developer by its ID, including related entities.
    Throws an exception if the developer is not found.
    */

    public Developer getById(Long id) {
        return developerRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Developer not found"));
    }

    /*
    Saves a new developer to the repository.
    */

    @Transactional
    public Developer save(DeveloperDTO dto) {
        if (developerRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Developer name already exists");
        }

        Developer newDeveloper = new Developer(dto.name());
        return developerRepository.save(newDeveloper);
    }

    /*
    Updates an existing developer's name.
    */

    @Transactional
    public Developer updateName(Long id, DeveloperDTO dto) {
        Developer existingDeveloper = getById(id);
        existingDeveloper.setName(dto.name());
        return developerRepository.save(existingDeveloper);
    }

    /*
    Deletes a developer by its ID.
    */

    @Transactional
    public void deleteById(Long id) {
        Developer existingDeveloper = getById(id);
        existingDeveloper.getGames().forEach(game -> game.setDeveloper(null));
        developerRepository.delete(existingDeveloper);
    }
}
