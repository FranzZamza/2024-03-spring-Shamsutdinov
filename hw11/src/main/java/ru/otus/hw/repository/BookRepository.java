package ru.otus.hw.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Book;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookCustomRepository {

    @NonNull
    @Override
    Mono<Void> delete(@NonNull Mono<Book> book);

    @NonNull
    @Override
    Mono<Void> save(@NonNull Mono<Book> book);
}