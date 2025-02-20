package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller for handling developer-related API requests.

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;

    // Endpoint to get all developers, accessible to authenticated users
    @GetMapping
    public ResponseEntity<List<Developer>> getAllDevelopers() {
        return ResponseEntity.ok(developerService.getAll());
    }

    // Endpoint to get a developer by ID, accessible to authenticated users
    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloper(@PathVariable Long id) {
        return ResponseEntity.ok(developerService.getById(id));
    }

    // Endpoint to create a new developer, accessible only by ADMIN role
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Developer> createDeveloper(@RequestBody Developer developer) {
        Developer savedDeveloper = developerService.save(developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeveloper);
    }

    // Endpoint to update an existing developer by ID, accessible only by ADMIN role
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Developer> updateDeveloper(@PathVariable Long id, @RequestBody Developer developer) {
        Developer existingDeveloper = developerService.getById(id);
        existingDeveloper.setName(developer.getName());
        Developer savedDeveloper = developerService.save(existingDeveloper);
        return ResponseEntity.ok(savedDeveloper);
    }

    // Endpoint to delete a developer by ID, accessible only by ADMIN role
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        developerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
