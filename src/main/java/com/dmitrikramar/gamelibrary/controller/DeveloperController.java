package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller for handling developer-related API requests.

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping
    public ResponseEntity<List<Developer>> getAllDevelopers() {
        return ResponseEntity.ok(developerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloper(@PathVariable Long id) {
        return ResponseEntity.ok(developerService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Developer> createDeveloper(@RequestBody Developer developer) {
        Developer savedDeveloper = developerService.save(developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeveloper);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Developer> updateDeveloper(@PathVariable Long id, @RequestBody Developer developer) {
        Developer existingDeveloper = developerService.getById(id);
        existingDeveloper.setName(developer.getName());
        Developer savedDeveloper = developerService.save(existingDeveloper);
        return ResponseEntity.ok(savedDeveloper);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        developerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
