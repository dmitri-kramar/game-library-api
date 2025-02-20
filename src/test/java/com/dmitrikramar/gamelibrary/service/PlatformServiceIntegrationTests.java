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
        testPlatform = new Platform("TestPlatform", null);
        platformRepository.save(testPlatform);
    }

    @Test
    void save() {
        Platform newPlatform = new Platform("NewPlatform", null);
        Platform savedPlatform = platformService.save(newPlatform);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getId()).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo("NewPlatform");
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
    void deleteById() {
        platformService.deleteById(testPlatform.getId());
        assertThat(platformRepository.findById(testPlatform.getId())).isEmpty();
    }
}
