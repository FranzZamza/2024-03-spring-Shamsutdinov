package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Void> delete(Mono<Book> book) {
        return book.flatMap(b -> {
            var removeBook = mongoTemplate.remove(b);
            var deleteCommentOfBook = mongoTemplate.remove(
                    new Query(Criteria.where("bookId").is(b.getId())),
                    Comment.class);
            return Mono.zip(removeBook, deleteCommentOfBook)
                    .then();
        });
    }

    @Override
    public Mono<Void> save(Mono<Book> book) {
        return book.
                flatMap(item -> {
                    var query = Query.query(Criteria.where("bookId").is(book.map(Book::getId)));
                    var update = new Update().set("book", book);
                    return Mono.zip(mongoTemplate.save(item), mongoTemplate.updateFirst(query, update, Book.class))
                            .then();
                });
    }
}
