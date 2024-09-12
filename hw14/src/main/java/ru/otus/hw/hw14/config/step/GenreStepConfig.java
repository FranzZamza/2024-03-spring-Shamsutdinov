package ru.otus.hw.hw14.config.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.hw14.config.BatchProperty;
import ru.otus.hw.hw14.config.step.cash.GenreCash;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

import static ru.otus.hw.hw14.config.step.util.StepName.GENRE_STEP_NAME;

@Configuration
@RequiredArgsConstructor
public class GenreStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BatchProperty batchProperty;
    private final GenreCash cash;

    private final static String READER_NAME = "genreReader";
    private final static String GENRE_COLLECTION = "genres";

    @Bean("genreReader")
    public JdbcPagingItemReader<Map<String, Object>> genreReader(DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<Map<String, Object>>()
                .name(READER_NAME)
                .dataSource(dataSource)
                .selectClause("select name")
                .fromClause("from genres")
                .rowMapper(new ColumnMapRowMapper())
                .sortKeys(Collections.singletonMap("name", Order.ASCENDING))
                .pageSize(batchProperty.getPagingSize())
                .build();
    }

    @Bean("genreProcessor")
    public ItemProcessor<Map<String, Object>, Map<String, Object>> authorProcessor() {
        return item -> item;
    }

    @Bean(name = "genreWriter")
    public ItemWriter<Map<String, Object>> genreWriter(MongoTemplate mongoTemplate) {
        return CashebleItemWriter.builder()
                .cash(cash)
                .collection(GENRE_COLLECTION)
                .mongoTemplate(mongoTemplate)
                .itemValueName("name")
                .build();
    }

    @Bean(name = GENRE_STEP_NAME)
    public Step step(@Qualifier("genreReader") ItemReader<Map<String, Object>> genreReader,
                     @Qualifier("genreProcessor") ItemProcessor<Map<String, Object>, Map<String, Object>> genreProcessor,
                     @Qualifier("genreWriter") ItemWriter<Map<String, Object>> genreWriter) {
        return new StepBuilder(GENRE_STEP_NAME, jobRepository)
                .<Map<String, Object>, Map<String, Object>>chunk(batchProperty.getChunkSize(), transactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .build();
    }
}
