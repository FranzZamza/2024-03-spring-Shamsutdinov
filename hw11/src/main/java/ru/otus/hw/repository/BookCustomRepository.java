package ru.otus.hw.repository;

import reactor.core.publisher.Mono;
import ru.otus.hw.model.Book;

public interface BookCustomRepository {

    Mono<Void> delete(Mono<Book> book);

    Mono<Void> save(Mono<Book> book);
}