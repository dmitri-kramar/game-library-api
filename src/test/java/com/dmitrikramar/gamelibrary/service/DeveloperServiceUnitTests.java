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
        // Creating a test developer object before each test
        testDeveloper = new Developer(1L, "Test Developer", null);
    }

    @Test
    void getAll_ShouldReturnDeveloperList() {
        // Mocking repository response
        when(developerRepository.findAllWithRelations()).thenReturn(List.of(testDeveloper));
        List<Developer> developers = developerService.getAll();
        assertEquals(1, developers.size());
        assertEquals("Test Developer", developers.get(0).getName());

        // Ensuring repository method is called once
        verify(developerRepository, times(1)).findAllWithRelations();
    }

    @Test
    void getById_ShouldReturnDeveloper_WhenExists() {
        // Mocking a successful find operation
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));
        Developer developer = developerService.getById(1L);
        assertEquals("Test Developer", developer.getName());
        verify(developerRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        // Mocking an empty result
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        // Expecting an exception when the developer is not found
        assertThrows(NoSuchElementException.class, () -> developerService.getById(1L));
    }

    @Test
    void save_ShouldReturnSavedDeveloper() {
        // Mocking repository save behavior
        when(developerRepository.save(testDeveloper)).thenReturn(testDeveloper);
        Developer savedDeveloper = developerService.save(testDeveloper);
        assertNotNull(savedDeveloper);
        assertEquals("Test Developer", savedDeveloper.getName());
        verify(developerRepository, times(1)).save(testDeveloper);
    }

    @Test
    void deleteById_ShouldDeleteDeveloper_WhenExists() {
        // Mocking a successful find operation
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testDeveloper));

        // Ensuring delete method doesn't throw exceptions
        doNothing().when(developerRepository).delete(testDeveloper);
        developerService.deleteById(1L);
        verify(developerRepository, times(1)).delete(testDeveloper);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        // Mocking an empty result
        when(developerRepository.findByIdWithRelations(1L)).thenReturn(Optional.empty());

        // Expecting an exception when trying to delete a non-existent developer
        assertThrows(NoSuchElementException.class, () -> developerService.deleteById(1L));
    }
}
