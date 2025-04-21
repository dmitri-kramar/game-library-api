package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.PlatformDTO;
import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.service.PlatformService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing platforms.
 * Provides endpoints to create, retrieve, update, and delete platforms.
 */
@RestController
@RequestMapping("/platforms")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformService platformService;

    /**
     * Retrieves a list of all platforms.
     * Accessible only to authenticated users.
     *
     * @return list of platforms with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Platform>> getAllPlatforms() {
        return ResponseEntity.ok(platformService.getAll());
    }

    /**
     * Retrieves a platform by its ID.
     * Accessible only to authenticated users.
     *
     * @param id the ID of the platform
     * @return the platform data with HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Platform> getPlatform(@PathVariable Long id) {
        return ResponseEntity.ok(platformService.getById(id));
    }

    /**
     * Creates a new platform.
     * Accessible only to users with ADMIN role.
     *
     * @param dto the platform data
     * @return the created platform with HTTP 201 status
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Platform> createPlatform(@Valid @RequestBody PlatformDTO dto) {
        Platform savedPlatform = platformService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlatform);
    }

    /**
     * Updates the name of an existing platform by ID.
     * Accessible only to users with ADMIN role.
     *
     * @param id  the ID of the platform to update
     * @param dto the updated platform data
     * @return the updated platform with HTTP 200 status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Platform> updatePlatform(@PathVariable Long id, @Valid @RequestBody PlatformDTO dto) {
        Platform updatedPlatform = platformService.updateNameById(id, dto);
        return ResponseEntity.ok(updatedPlatform);
    }

    /**
     * Deletes a platform by its ID.
     * Accessible only to users with ADMIN role.
     *
     * @param id the ID of the platform to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlatform(@PathVariable Long id) {
        platformService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
