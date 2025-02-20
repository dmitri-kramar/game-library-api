package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service class responsible for business logic related to Genre entities.

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    // Retrieves a list of all genres, including their related entities.
    public List<Genre> getAll() {
        return genreRepository.findAllWithRelations();
    }

    // Retrieves a genre by its ID, including related entities.
    // Throws an exception if the genre is not found.
    public Genre getById(Long id) {
        return genreRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Genre not found"));
    }

    // Saves a new or existing genre to the repository.
    @Transactional
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    // Deletes a genre by its ID.
    @Transactional
    public void deleteById(Long id) {
        genreRepository.delete(getById(id));
    }
}
