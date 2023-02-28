package com.example.demo;


import com.example.demo.repositories.FileRepository;
import com.example.demo.services.FileService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public FileRepository fileRepository() {
        return Mockito.mock(FileRepository.class);
    }

    @Bean
    @Primary
    public FileService fileService() {
        return Mockito.mock(FileService.class);
    }
}
