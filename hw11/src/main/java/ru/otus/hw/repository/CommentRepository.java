package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.model.Comment;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByBookId(String bookId);
}

