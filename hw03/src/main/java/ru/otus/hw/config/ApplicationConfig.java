package ru.otus.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@EnableConfigurationProperties(AppProperties.class)
public class ApplicationConfig {
}
