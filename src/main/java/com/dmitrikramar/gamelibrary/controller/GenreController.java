package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.GenreDTO;
import com.dmitrikramar.gamelibrary.entity.Genre;
import com.dmitrikramar.gamelibrary.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing genres.
 * Provides endpoints to create, retrieve, update, and delete genres.
 */
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    /**
     * Retrieves a list of all genres.
     * Accessible only to authenticated users.
     *
     * @return list of genres with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAll());
    }

    /**
     * Retrieves a genre by its ID.
     * Accessible only to authenticated users.
     *
     * @param id the ID of the genre
     * @return the genre data with HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    /**
     * Creates a new genre.
     * Accessible only to users with ADMIN role.
     *
     * @param dto the genre data
     * @return the created genre with HTTP 201 status
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody GenreDTO dto) {
        Genre savedGenre = genreService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
    }

    /**
     * Updates the name of an existing genre by ID.
     * Accessible only to users with ADMIN role.
     *
     * @param id  the ID of the genre to update
     * @param dto the updated genre data
     * @return the updated genre with HTTP 200 status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreDTO dto) {
        Genre updatedGenre = genreService.updateNameById(id, dto);
        return ResponseEntity.ok(updatedGenre);
    }

    /**
     * Deletes a genre by its ID.
     * Accessible only to users with ADMIN role.
     *
     * @param id the ID of the genre to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
