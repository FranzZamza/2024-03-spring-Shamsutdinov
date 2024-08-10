package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void delete(Book book) {
        mongoTemplate.execute(session -> {
            mongoTemplate.remove(book);
            mongoTemplate.remove(
                    new Query(Criteria.where("bookId").is(book.getId())),
                    Comment.class);
            return session;
        });
    }

    @Override
    public Book save(Book book) {
        mongoTemplate.execute((session) -> {
            var query = Query.query(Criteria.where("bookId").is(book.getId()));
            var update = new Update().set("book", book);
            mongoTemplate.updateMulti(query, update, Comment.class);
            return mongoTemplate.save(book);

        });
        return book;
    }

}
