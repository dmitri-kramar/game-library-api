package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PlatformServiceIntegrationTests {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PlatformRepository platformRepository;

    private Platform testPlatform;

    @BeforeEach
    void setUp() {
        // Creates and saves a test platform before each test
        testPlatform = new Platform("TestPlatform", null);
        platformRepository.save(testPlatform);
    }

    @Test
    void save() {
        // Verifies that a new platform is saved correctly with a generated ID
        Platform newPlatform = new Platform("NewPlatform", null);
        Platform savedPlatform = platformService.save(newPlatform);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getId()).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo("NewPlatform");
    }

    @Test
    void getAll() {
        // Verifies that all platforms are retrieved, including the test platform
        List<Platform> platforms = platformService.getAll();
        assertThat(platforms).isNotEmpty();
        assertThat(platforms).contains(testPlatform);
    }

    @Test
    void getById() {
        // Verifies that the platform is retrieved correctly by ID
        Platform foundPlatform = platformService.getById(testPlatform.getId());

        assertThat(foundPlatform).isNotNull();
        assertThat(foundPlatform.getId()).isEqualTo(testPlatform.getId());
    }

    @Test
    void deleteById() {
        // Verifies that a platform is deleted correctly and no longer exists in the repository
        platformService.deleteById(testPlatform.getId());
        assertThat(platformRepository.findById(testPlatform.getId())).isEmpty();
    }
}
