package ru.otus.hw.repository;



import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> findAll();

    Optional<Genre> findById(long id);
}