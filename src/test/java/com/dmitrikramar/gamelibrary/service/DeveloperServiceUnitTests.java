package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.DeveloperDTO;
import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceUnitTests {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    private Developer testDeveloper;
    private DeveloperDTO testDeveloperDTO;

    @BeforeEach
    void setUp() {
        testDeveloper = new Developer(1L, "Test Developer", new HashSet<>());
        testDeveloperDTO = new DeveloperDTO("Test Developer");
    }

    @Test
    void getAll_ShouldReturnDeveloperList() {
        when(developerRepository.findAllWithRelations()).thenReturn(List.of(testDeveloper));

        List<Developer> developers = developerService.getAll();

        assertThat(developers).hasSize(1);
        assertThat(developers.get(0).getName()).isEqualTo("Test Developer");
        verify(developerRepository).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnDeveloper_WhenExists() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));

        Developer developer = developerService.getById(1L);

        assertThat(developer.getName()).isEqualTo("Test Developer");
        verify(developerRepository).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> developerService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedDeveloper() {
        when(developerRepository.save(any(Developer.class))).thenReturn(testDeveloper);

        Developer savedDeveloper = developerService.save(testDeveloperDTO);

        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getName()).isEqualTo("Test Developer");
        verify(developerRepository).save(any(Developer.class));
    }

    @Test
    void updateNameById_ShouldReturnUpdatedDeveloper() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));
        when(developerRepository.save(any(Developer.class))).thenReturn(testDeveloper);

        DeveloperDTO updated = new DeveloperDTO("Updated Developer");
        Developer updatedDeveloper = developerService.updateNameById(1L, updated);

        assertThat(updatedDeveloper.getName()).isEqualTo("Updated Developer");
        verify(developerRepository).findByIdWithRelations(1L);
        verify(developerRepository).save(testDeveloper);
    }

    @Test
    void updateNameById_ShouldThrowException_WhenNameAlreadyExists() {
        DeveloperDTO duplicateNameDTO = new DeveloperDTO("Existing Developer");

        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));
        when(developerRepository.existsByName("Existing Developer")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                developerService.updateNameById(1L, duplicateNameDTO));
    }


    @Test
    void deleteById_ShouldDeleteDeveloper_WhenExists() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));
        doNothing().when(developerRepository).delete(testDeveloper);

        developerService.deleteById(1L);

        verify(developerRepository).delete(testDeveloper);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> developerService.deleteById(1L));
    }
}
