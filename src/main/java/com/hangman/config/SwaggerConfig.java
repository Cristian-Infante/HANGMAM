package com.hangman.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/Game/**"};
        return GroupedOpenApi.builder()
                .group("Game")
                .pathsToMatch(paths)
                .build();
    }
}
