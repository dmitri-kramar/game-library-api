package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// Service layer for managing genres. Provides basic CRUD operations.

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getAll() {
        return genreRepository.findAllWithRelations();
    }

    public Genre getById(Long id) {
        return genreRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NoSuchElementException("Genre not found"));
    }

    @Transactional
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Transactional
    public void deleteById(Long id) {
        genreRepository.delete(getById(id));
    }
}
