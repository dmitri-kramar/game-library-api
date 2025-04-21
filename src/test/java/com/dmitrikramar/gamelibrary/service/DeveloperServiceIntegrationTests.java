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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        testDeveloper = new Developer("Test Developer");
        developerRepository.save(testDeveloper);
    }

    @Test
    void getAll() {
        List<Developer> developers = developerService.getAll();

        assertThat(developers).isNotEmpty();
        assertThat(developers).contains(testDeveloper);
    }

    @Test
    void getById() {
        Developer foundDeveloper = developerService.getById(testDeveloper.getId());

        assertThat(foundDeveloper).isNotNull();
        assertThat(foundDeveloper.getId()).isEqualTo(testDeveloper.getId());
    }

    @Test
    void save() {
        DeveloperDTO newDeveloper = new DeveloperDTO("New Developer");
        Developer savedDeveloper = developerService.save(newDeveloper);

        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getId()).isNotNull();
        assertThat(savedDeveloper.getName()).isEqualTo("New Developer");
    }

    @Test
    void updateNameById() {
        DeveloperDTO updated = new DeveloperDTO("Updated Developer");
        Developer updatedDeveloper = developerService.updateNameById(testDeveloper.getId(), updated);

        assertThat(updatedDeveloper).isNotNull();
        assertThat(updatedDeveloper.getName()).isEqualTo("Updated Developer");
    }

    @Test
    void updateNameById_ShouldThrowException_WhenNameAlreadyExists() {
        Developer existing = new Developer("Existing Developer");
        developerRepository.save(existing);

        DeveloperDTO duplicate = new DeveloperDTO("Existing Developer");

        assertThrows(IllegalArgumentException.class,
                () -> developerService.updateNameById(testDeveloper.getId(), duplicate));
    }

    @Test
    void deleteById() {
        developerService.deleteById(testDeveloper.getId());

        assertThat(developerRepository.findById(testDeveloper.getId())).isEmpty();
    }
}
