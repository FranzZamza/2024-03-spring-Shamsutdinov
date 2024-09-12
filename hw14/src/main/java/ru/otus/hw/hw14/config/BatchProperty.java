package ru.otus.hw.hw14.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch")
@Getter
@Setter
public class BatchProperty {
    private Integer chunkSize;
    private Integer pagingSize;
}
