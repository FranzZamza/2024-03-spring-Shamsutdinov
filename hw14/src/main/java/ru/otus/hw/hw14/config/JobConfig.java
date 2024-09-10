package ru.otus.hw.hw14.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.otus.hw.hw14.config.step.util.StepName.AUTHOR_STEP_NAME;
import static ru.otus.hw.hw14.config.step.util.StepName.BOOK_STEP_NAME;
import static ru.otus.hw.hw14.config.step.util.StepName.GENRE_STEP_NAME;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobRepository jobRepository;
    private final static String JOB_NAME = "TRANSFER_JOB";

    @Bean
    public Job job(@Qualifier(BOOK_STEP_NAME) Step bookStep,
                   @Qualifier(AUTHOR_STEP_NAME) Step authorStep,
                   @Qualifier(GENRE_STEP_NAME) Step genreStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .flow(authorStep)
                .next(genreStep)
                .next(bookStep)
                .end().build();
    }
}
