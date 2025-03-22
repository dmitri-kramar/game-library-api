package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.service.PlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Controller for handling platform-related API requests.
*/

@RestController
@RequestMapping("/platforms")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformService platformService;

    /*
    Endpoint to get all platforms, accessible to authenticated users
    */

    @Operation(summary = "Get all platforms (USER)", description = "Retrieves a list of all platforms.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of platforms"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required")
    })
    @GetMapping
    public ResponseEntity<List<Platform>> getAllPlatforms() {
        return ResponseEntity.ok(platformService.getAll());
    }

    /*
    Endpoint to get a platform by ID, accessible to authenticated users
    */

    @GetMapping("/{id}")
    public ResponseEntity<Platform> getPlatform(@PathVariable Long id) {
        return ResponseEntity.ok(platformService.getById(id));
    }

    /*
    Endpoint to create a new platform, accessible only by ADMIN role
    */

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Platform> createPlatform(@RequestBody Platform platform) {
        Platform savedPlatform = platformService.save(platform);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlatform);
    }

    /*
    Endpoint to updateName an existing platform by ID, accessible only by ADMIN role
    */

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Platform> updatePlatform(@PathVariable Long id, @RequestBody Platform platform) {
        Platform existingPlatform = platformService.getById(id);
        existingPlatform.setName(platform.getName());
        Platform savedPlatform = platformService.save(existingPlatform);
        return ResponseEntity.ok(savedPlatform);
    }

    /*
    Endpoint to delete a platform by ID, accessible only by ADMIN role
    */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlatform(@PathVariable Long id) {
        platformService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

