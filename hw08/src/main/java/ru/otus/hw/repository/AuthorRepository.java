package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Author;

@Repository
public interface AuthorRepository  extends MongoRepository<Author, String> {
}