package ru.otus.hw.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Genre;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {

}