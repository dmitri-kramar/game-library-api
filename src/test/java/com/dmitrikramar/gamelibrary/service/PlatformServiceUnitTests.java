package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatformServiceUnitTests {

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private PlatformService platformService;

    private Platform testPlatform;

    @BeforeEach
    void setUp() {
        testPlatform = new Platform(1L, "Test Platform", null);
    }

    @Test
    void getAll_ShouldReturnPlatformList() {
        when(platformRepository.findAllWithRelations()).thenReturn(List.of(testPlatform));
        List<Platform> platforms = platformService.getAll();
        assertEquals(1, platforms.size());
        assertEquals("Test Platform", platforms.get(0).getName());
        verify(platformRepository, times(1)).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnPlatform_WhenExists() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testPlatform));
        Platform platform = platformService.getById(1L);
        assertEquals("Test Platform", platform.getName());
        verify(platformRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> platformService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedPlatform() {
        when(platformRepository.save(testPlatform)).thenReturn(testPlatform);
        Platform savedPlatform = platformService.save(testPlatform);
        assertNotNull(savedPlatform);
        assertEquals("Test Platform", savedPlatform.getName());
        verify(platformRepository, times(1)).save(testPlatform);
    }

    @Test
    void deleteById_ShouldDeletePlatform_WhenExists() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testPlatform));
        doNothing().when(platformRepository).delete(testPlatform);
        platformService.deleteById(1L);
        verify(platformRepository, times(1)).delete(testPlatform);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(platformRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> platformService.deleteById(1L));
    }
}
