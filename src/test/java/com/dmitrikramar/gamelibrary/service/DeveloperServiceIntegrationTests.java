package com.dmitrikramar.gamelibrary.service;

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
        testDeveloper = new Developer("TestDeveloper", null);
        developerRepository.save(testDeveloper);
    }

    @Test
    void save() {
        Developer newDeveloper = new Developer("NewDeveloper", null);
        Developer savedDeveloper = developerService.save(newDeveloper);

        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getId()).isNotNull();
        assertThat(savedDeveloper.getName()).isEqualTo("NewDeveloper");
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
    void deleteById() {
        developerService.deleteById(testDeveloper.getId());
        assertThat(developerRepository.findById(testDeveloper.getId())).isEmpty();
    }
}
