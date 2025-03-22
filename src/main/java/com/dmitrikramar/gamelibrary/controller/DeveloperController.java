package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.dto.DeveloperDTO;
import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.service.DeveloperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Controller for handling developer-related API requests.
*/

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
@Tag(name = "Developers")
public class DeveloperController {

    private final DeveloperService developerService;

    /*
    Endpoint to get all developers, accessible to authenticated users
    */

    @Operation(summary = "Get all developers (available only for AUTHENTICATED users)",
            description = "Returns a list of all developers or an empty list if no developers are found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of developers retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<List<Developer>> getAllDevelopers() {
        return ResponseEntity.ok(developerService.getAll());
    }

    /*
    Endpoint to get a developer by ID, accessible to authenticated users
    */

    @Operation(summary = "Get a developer by ID (available only for AUTHENTICATED users)",
            description = "Returns the developer if his ID is present in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Developer with the given ID was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Developer> getDeveloper(@PathVariable Long id) {
        return ResponseEntity.ok(developerService.getById(id));
    }

    /*
    Endpoint to create a new developer, accessible only by ADMIN
    */

    @Operation(summary = "Add a new developer (available only for ADMIN)",
            description = "Adds a new developer to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Developer added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden – Access denied",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Developer> createDeveloper(@Valid @RequestBody DeveloperDTO dto) {
        Developer savedDeveloper = developerService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeveloper);
    }

    /*
    Endpoint to update an existing developer's name, accessible only by ADMIN
    */

    @Operation(summary = "Update an existing developer's name (available only for ADMIN)",
            description = "Returns the developer with the updated name if his ID is present in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer's name updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden – Access denied",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Developer with the given ID was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Developer> updateDeveloperName(@PathVariable Long id, @Valid @RequestBody DeveloperDTO dto) {
        Developer updatedDeveloper = developerService.updateName(id, dto);
        return ResponseEntity.ok(updatedDeveloper);
    }

    /*
    Endpoint to delete a developer by ID, accessible only by ADMIN
    */

    @Operation(summary = "Delete an existing developer (available only for ADMIN)",
            description = "Deletes the developer if his ID is present in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Developer deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Authentication is required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden – Access denied",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Developer with the given ID was not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        developerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
