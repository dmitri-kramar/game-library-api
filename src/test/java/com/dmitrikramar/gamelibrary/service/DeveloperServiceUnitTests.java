package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
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
class DeveloperServiceUnitTests {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    private Developer testDeveloper;

    @BeforeEach
    void setUp() {
        testDeveloper = new Developer(1L, "Test Developer", null);
    }

    @Test
    void getAll_ShouldReturnDeveloperList() {
        when(developerRepository.findAllWithRelations()).thenReturn(List.of(testDeveloper));
        List<Developer> developers = developerService.getAll();
        assertEquals(1, developers.size());
        assertEquals("Test Developer", developers.get(0).getName());
        verify(developerRepository, times(1)).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnDeveloper_WhenExists() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));
        Developer developer = developerService.getById(1L);
        assertEquals("Test Developer", developer.getName());
        verify(developerRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> developerService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedDeveloper() {
        when(developerRepository.save(testDeveloper)).thenReturn(testDeveloper);
        Developer savedDeveloper = developerService.save(testDeveloper);
        assertNotNull(savedDeveloper);
        assertEquals("Test Developer", savedDeveloper.getName());
        verify(developerRepository, times(1)).save(testDeveloper);
    }

    @Test
    void deleteById_ShouldDeleteDeveloper_WhenExists() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));
        doNothing().when(developerRepository).delete(testDeveloper);
        developerService.deleteById(1L);
        verify(developerRepository, times(1)).delete(testDeveloper);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> developerService.deleteById(1L));
    }
}
