package ru.otus.hw.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

@Component
@RequiredArgsConstructor
public class BookCascadeDeleteMongoEventListener extends AbstractMongoEventListener<Book> {

    private final MongoOperations operations;

    @Override
    public void onBeforeDelete(@NonNull BeforeDeleteEvent<Book> event) {
        var document = event.getSource();
        var bookId = document.getString("_id");
        var query = new Query(Criteria.where("book_id").is(bookId));
        operations.remove(query,Comment.class);
    }
}
