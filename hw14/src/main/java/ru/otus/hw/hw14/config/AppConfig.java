package ru.otus.hw.hw14.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BatchProperty.class)
public class AppConfig {

}
