package ru.otus.hw.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String>, BookCustomRepository {
    @Override
    void delete(@NonNull Book book);

    @Override
    @NonNull
    Book save(@NonNull Book book);
}