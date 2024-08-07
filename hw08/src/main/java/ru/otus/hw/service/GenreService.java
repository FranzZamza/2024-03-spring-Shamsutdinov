package ru.otus.hw.service;


import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> findAll();

    Optional<Genre> findById(String id);
}
