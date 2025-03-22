package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.DeveloperDTO;
import com.dmitrikramar.gamelibrary.entity.Developer;
import com.dmitrikramar.gamelibrary.repository.DeveloperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DeveloperServiceIntegrationTests {

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private DeveloperRepository developerRepository;

    private Developer testDeveloper;

    @BeforeEach
    void setUp() {
        // Creates and saves a test developer before each test
        testDeveloper = new Developer("TestDeveloper");
        developerRepository.save(testDeveloper);
    }

    @Test
    void save() {
        // Tests the save functionality of the DeveloperService
        DeveloperDTO newDeveloper = new DeveloperDTO("NewDeveloper");
        Developer savedDeveloper = developerService.save(newDeveloper);

        // Verifies the saved developer is not null, has an ID, and the name is correct
        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getId()).isNotNull();
        assertThat(savedDeveloper.getName()).isEqualTo("NewDeveloper");
    }

    @Test
    void updateName() {
        DeveloperDTO newDeveloper = new DeveloperDTO("NewDeveloper");
        Developer updatedDeveloper = developerService.updateName(testDeveloper.getId(), newDeveloper);
        assertThat(updatedDeveloper.getName()).isEqualTo("NewDeveloper");
    }

    @Test
    void getAll() {
        // Tests the retrieval of all developers through the DeveloperService
        List<Developer> developers = developerService.getAll();

        // Verifies that the list is not empty and contains the test developer
        assertThat(developers).isNotEmpty();
        assertThat(developers).contains(testDeveloper);
    }

    @Test
    void getById() {
        // Tests the retrieval of a developer by ID through the DeveloperService
        Developer foundDeveloper = developerService.getById(testDeveloper.getId());

        // Verifies that the developer was found and its ID matches the expected ID
        assertThat(foundDeveloper).isNotNull();
        assertThat(foundDeveloper.getId()).isEqualTo(testDeveloper.getId());
    }

    @Test
    void deleteById() {
        // Tests the deletion of a developer by ID through the DeveloperService
        developerService.deleteById(testDeveloper.getId());

        // Verifies that the developer was deleted and no longer exists in the repository
        assertThat(developerRepository.findById(testDeveloper.getId())).isEmpty();
    }
}
