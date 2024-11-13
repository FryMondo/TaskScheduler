package com.example.TaskScheduler.models;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ModelConfig {

    @Bean
    @Scope("prototype")
    public Task task() {
        return new Task();
    }

    @Bean
    @Scope("prototype")
    public User user() {
        return new User();
    }
}

