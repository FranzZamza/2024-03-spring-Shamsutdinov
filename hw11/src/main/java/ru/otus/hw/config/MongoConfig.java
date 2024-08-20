package ru.otus.hw.config;

import com.github.cloudyrock.spring.v5.EnableMongock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ru.otus.hw.repository")
@RequiredArgsConstructor
@EnableMongock
public class MongoConfig {
}
