package ru.otus.hw.hw14.config.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.hw14.config.BatchProperty;
import ru.otus.hw.hw14.config.step.cash.AuthorCash;
import ru.otus.hw.hw14.config.step.cash.GenreCash;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

import static ru.otus.hw.hw14.config.step.util.StepName.BOOK_STEP_NAME;

@Configuration
@RequiredArgsConstructor
public class BookStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BatchProperty batchProperty;

    private final static String READER_NAME = "bookPagingReader";
    private final static String BOOK_COLLECTION = "books";


    @Bean("bookReader")
    public JdbcPagingItemReader<Map<String, Object>> bookReader(DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<Map<String, Object>>()
                .name(READER_NAME)
                .dataSource(dataSource)
                .selectClause("select title, authors.full_name, genres.name ")
                .fromClause("from books " +
                        "left join authors on author_id=authors.id " +
                        "left join genres on genre_id=genres.id")
                .rowMapper(new ColumnMapRowMapper())
                .sortKeys(Collections.singletonMap("title", Order.ASCENDING))
                .pageSize(batchProperty.getPagingSize())
                .build();
    }

    @Bean("bookProcessor")
    public ItemProcessor<Map<String, Object>, Map<String, Object>> bookProcessor(AuthorCash authorCash, GenreCash genreCash) {
        return item -> bookRowMapper(authorCash, genreCash, item);
    }

    @Bean("bookWriter")
    public ItemWriter<Object> bookWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<>()
                .collection(BOOK_COLLECTION)
                .template(mongoOperations)
                .mode(MongoItemWriter.Mode.INSERT)
                .build();
    }

    @Bean(name = BOOK_STEP_NAME)
    public Step step(@Qualifier("bookReader") ItemReader<Map<String, Object>> bookReader,
                     @Qualifier("bookProcessor") ItemProcessor<Map<String, Object>, Map<String, Object>> bookProcessor,
                     @Qualifier("bookWriter") ItemWriter<Object> bookWriter) {

        return new StepBuilder(BOOK_STEP_NAME, jobRepository)
                .<Map<String, Object>, Map<String, Object>>chunk(batchProperty.getChunkSize(), transactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    private Map<String, Object> bookRowMapper(AuthorCash authorCash, GenreCash genreCash, Map<String, Object> item) {
        return Map.of
                ("title", item.get("title"),
                        "author_id", authorCash.getId(item.get("full_name").toString()),
                        "genre_id", genreCash.getId(item.get("name").toString()));
    }
}
