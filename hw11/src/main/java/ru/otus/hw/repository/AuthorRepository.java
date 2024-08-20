package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Author;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Mono<Author> findByFullName(String fullName);
}