package ru.otus.hw.service;

import ru.otus.hw.model.Book;

import java.util.List;

public interface BookService {
    Book findById(long id);

    List<Book> findAll();

    Book save(Book book);

    Book update(Book book);

    void deleteById(long id);
}
