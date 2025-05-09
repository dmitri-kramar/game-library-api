package com.dmitrikramar.gamelibrary.service;

import com.dmitrikramar.gamelibrary.dto.PlatformDTO;
import com.dmitrikramar.gamelibrary.entity.Platform;
import com.dmitrikramar.gamelibrary.repository.PlatformRepository;
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
class PlatformServiceIntegrationTests {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PlatformRepository platformRepository;

    private Platform testPlatform;

    @BeforeEach
    void setUp() {
        testPlatform = new Platform("Test Platform");
        platformRepository.save(testPlatform);
    }

    @Test
    void getAll() {
        List<Platform> platforms = platformService.getAll();

        assertThat(platforms).isNotEmpty();
        assertThat(platforms).contains(testPlatform);
    }

    @Test
    void getById() {
        Platform foundPlatform = platformService.getById(testPlatform.getId());

        assertThat(foundPlatform).isNotNull();
        assertThat(foundPlatform.getId()).isEqualTo(testPlatform.getId());
    }

    @Test
    void save() {
        PlatformDTO newPlatform = new PlatformDTO("New Platform");
        Platform savedPlatform = platformService.save(newPlatform);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getId()).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo("New Platform");
    }

    @Test
    void updateNameById() {
        PlatformDTO updated = new PlatformDTO("Updated Platform");
        Platform updatedPlatform = platformService.updateNameById(testPlatform.getId(), updated);

        assertThat(updatedPlatform).isNotNull();
        assertThat(updatedPlatform.getName()).isEqualTo("Updated Platform");
    }

    @Test
    void updateNameById_ShouldThrowException_WhenNameAlreadyExists() {
        Platform existing = new Platform("Existing Platform");
        platformRepository.save(existing);

        PlatformDTO duplicate = new PlatformDTO("Existing Platform");

        assertThrows(IllegalArgumentException.class,
                () -> platformService.updateNameById(testPlatform.getId(), duplicate));
    }

    @Test
    void deleteById() {
        platformService.deleteById(testPlatform.getId());

        assertThat(platformRepository.findById(testPlatform.getId())).isEmpty();
    }
}
