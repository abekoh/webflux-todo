package dev.abekoh.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Clock;

@Configuration
@EnableR2dbcRepositories
public class AppConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
