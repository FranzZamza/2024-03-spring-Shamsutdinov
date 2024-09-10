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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.hw14.config.BatchProperty;
import ru.otus.hw.hw14.config.step.cash.AuthorCash;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

import static ru.otus.hw.hw14.config.step.util.StepName.AUTHOR_STEP_NAME;

@Configuration
@RequiredArgsConstructor
public class AuthorStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BatchProperty batchProperty;
    private final static String READER_NAME = "authorReader";
    private final static String AUTHOR_COLLECTION = "authors";


    @Bean("authorReader")
    public JdbcPagingItemReader<Map<String, Object>> authorReader(DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<Map<String, Object>>()
                .name(READER_NAME)
                .dataSource(dataSource)
                .selectClause("select full_name")
                .fromClause("from authors")
                .rowMapper(new ColumnMapRowMapper())
                .sortKeys(Collections.singletonMap("full_name", Order.ASCENDING))
                .pageSize(batchProperty.getPagingSize())
                .build();
    }

    @Bean("authorProcessor")
    public ItemProcessor<Map<String, Object>, Map<String, Object>> authorProcessor() {
        return item -> item;
    }

    @Bean(name = "authorWriter")
    public ItemWriter<Map<String, Object>> authorWriter(AuthorCash cash, MongoTemplate mongoTemplate) {
        return CashebleItemWriter.builder()
                .cash(cash)
                .collection(AUTHOR_COLLECTION)
                .mongoTemplate(mongoTemplate)
                .itemValueName("full_name")
                .build();
    }

    @Bean(name = AUTHOR_STEP_NAME)
    public Step step(@Qualifier("authorReader") ItemReader<Map<String, Object>> authorReader,
                     @Qualifier("authorProcessor") ItemProcessor<Map<String, Object>, Map<String, Object>> authorProcessor,
                     @Qualifier("authorWriter") ItemWriter<Map<String, Object>> authorWriter) {
        return new StepBuilder(AUTHOR_STEP_NAME, jobRepository)
                .<Map<String, Object>, Map<String, Object>>chunk(batchProperty.getChunkSize(), transactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

}
