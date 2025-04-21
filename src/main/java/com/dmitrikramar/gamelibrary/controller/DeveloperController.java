package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.DeveloperDTO;
import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.service.DeveloperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing developers.
 * Provides endpoints to create, retrieve, update, and delete developers.
 */
@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;

    /**
     * Retrieves a list of all developers.
     * Accessible only to authenticated users.
     *
     * @return list of developers with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Developer>> getAllDevelopers() {
        return ResponseEntity.ok(developerService.getAll());
    }

    /**
     * Retrieves a developer by their ID.
     * Accessible only to authenticated users.
     *
     * @param id the ID of the developer
     * @return the developer data with HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloper(@PathVariable Long id) {
        return ResponseEntity.ok(developerService.getById(id));
    }

    /**
     * Creates a new developer.
     * Accessible only to users with ADMIN role.
     *
     * @param dto the developer data
     * @return the created developer with HTTP 201 status
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Developer> createDeveloper(@Valid @RequestBody DeveloperDTO dto) {
        Developer savedDeveloper = developerService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeveloper);
    }

    /**
     * Updates the name of an existing developer.
     * Accessible only to users with ADMIN role.
     *
     * @param id  the ID of the developer to update
     * @param dto the updated developer data
     * @return the updated developer with HTTP 200 status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Developer> updateDeveloperName(@PathVariable Long id, @Valid @RequestBody DeveloperDTO dto) {
        Developer updatedDeveloper = developerService.updateNameById(id, dto);
        return ResponseEntity.ok(updatedDeveloper);
    }

    /**
     * Deletes a developer by their ID.
     * Accessible only to users with ADMIN role.
     *
     * @param id the ID of the developer to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        developerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
