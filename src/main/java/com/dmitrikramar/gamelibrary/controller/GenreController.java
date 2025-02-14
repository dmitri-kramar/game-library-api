package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller for handling genre-related API requests.

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> genres = genreService.getAll();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        return genreService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        Genre savedGenre = genreService.save(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        return genreService.getById(id)
                .map(existingGenre -> {
                    existingGenre.setName(genre.getName());
                    Genre updatedGenre = genreService.save(existingGenre);
                    return ResponseEntity.ok(updatedGenre);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        if (genreService.existsById(id)) {
            genreService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
