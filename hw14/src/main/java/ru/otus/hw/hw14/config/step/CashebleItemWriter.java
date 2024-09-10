package ru.otus.hw.hw14.config.step;

import lombok.Builder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.hw14.config.step.cash.Cash;

import java.util.Collection;
import java.util.Map;

@Builder
public class CashebleItemWriter implements ItemWriter<Map<String, Object>> {

    private final MongoTemplate mongoTemplate;
    private final String collection;
    private final Cash cash;
    private final String itemValueName;

    private final static String MONGO_ID = "_id";

    @Override
    public void write(Chunk<? extends Map<String, Object>> chunk) throws Exception {
        var contextItems = mongoTemplate.insert(chunk.getItems(), collection);
        saveToCash(contextItems);
    }

    private void saveToCash(Collection<? extends Map<String, Object>> contextItems) {
        contextItems.forEach(item -> cash.add(item.get(itemValueName).toString(), item.get(MONGO_ID).toString()));
    }
}
