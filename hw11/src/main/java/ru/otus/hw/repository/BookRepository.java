package ru.otus.hw.repository;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Book;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookCustomRepository {

    @Query(value = "{'_id': ?0}", fields = "{'comments': {$slice: ?1} }")
    Mono<Book> findBookByIdWithCommentsLimit(@Param("id") String id, @NonNull int commentsLimit);

    @NonNull
    @Query(value = "{}", fields = "{'comments': 0}")
    Flux<Book> findAll();

    @NonNull
    @Override
    Mono<Void> delete(@NonNull Mono<Book> book);

    @NonNull
    @Override
    Mono<Void> save(@NonNull Mono<Book> book);
}