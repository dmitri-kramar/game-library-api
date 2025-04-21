package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.PlatformDTO;
import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
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
class PlatformServiceUnitTests {

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private PlatformService platformService;

    private Platform testPlatform;
    private PlatformDTO testPlatformDTO;

    @BeforeEach
    void setUp() {
        testPlatform = new Platform(1L, "Test Platform", new HashSet<>());
        testPlatformDTO = new PlatformDTO("Test Platform");
    }

    @Test
    void getAll_ShouldReturnPlatformList() {
        when(platformRepository.findAllWithRelations()).thenReturn(List.of(testPlatform));

        List<Platform> platforms = platformService.getAll();

        assertThat(platforms).hasSize(1);
        assertThat(platforms.get(0).getName()).isEqualTo("Test Platform");
        verify(platformRepository).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnPlatform_WhenExists() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testPlatform));

        Platform platform = platformService.getById(1L);

        assertThat(platform.getName()).isEqualTo("Test Platform");
        verify(platformRepository).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> platformService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedPlatform() {
        when(platformRepository.save(any(Platform.class))).thenReturn(testPlatform);

        Platform savedPlatform = platformService.save(testPlatformDTO);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo("Test Platform");
        verify(platformRepository).save(any(Platform.class));
    }

    @Test
    void updateNameById_ShouldReturnUpdatedPlatform() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testPlatform));
        when(platformRepository.save(any(Platform.class))).thenReturn(testPlatform);

        PlatformDTO updated = new PlatformDTO("Updated Platform");
        Platform updatedPlatform = platformService.updateNameById(1L, updated);

        assertThat(updatedPlatform.getName()).isEqualTo("Updated Platform");
        verify(platformRepository).findByIdWithRelations(1L);
        verify(platformRepository).save(testPlatform);
    }

    @Test
    void updateNameById_ShouldThrowException_WhenNameAlreadyExists() {
        PlatformDTO duplicateNameDTO = new PlatformDTO("Existing Platform");

        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testPlatform));
        when(platformRepository.existsByName("Existing Platform")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                platformService.updateNameById(1L, duplicateNameDTO));
    }


    @Test
    void deleteById_ShouldDeletePlatform_WhenExists() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testPlatform));
        doNothing().when(platformRepository).delete(testPlatform);

        platformService.deleteById(1L);

        verify(platformRepository).delete(testPlatform);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> platformService.deleteById(1L));
    }
}
