package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.service.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller for handling platform-related API requests.

@RestController
@RequestMapping("/platforms")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformService platformService;

    @GetMapping
    public ResponseEntity<List<Platform>> getAllPlatforms() {
        return ResponseEntity.ok(platformService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Platform> getPlatform(@PathVariable Long id) {
        return ResponseEntity.ok(platformService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Platform> createPlatform(@RequestBody Platform platform) {
        Platform savedPlatform = platformService.save(platform);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlatform);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Platform> updatePlatform(@PathVariable Long id, @RequestBody Platform platform) {
        Platform existingPlatform = platformService.getById(id);
        existingPlatform.setName(platform.getName());
        Platform savedPlatform = platformService.save(existingPlatform);
        return ResponseEntity.ok(savedPlatform);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlatform(@PathVariable Long id) {
        platformService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

