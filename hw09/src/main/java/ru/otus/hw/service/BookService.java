package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    void save(BookDto book);

    void update(BookDto book);

    void deleteById(long id);
}
