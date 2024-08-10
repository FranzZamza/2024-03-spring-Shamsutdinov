package ru.otus.hw.service;


import ru.otus.hw.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAll();

    Optional<Author> findById(String id);
}
