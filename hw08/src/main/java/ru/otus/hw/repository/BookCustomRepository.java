package ru.otus.hw.repository;

import ru.otus.hw.model.Book;

public interface BookCustomRepository {

    void delete(Book book);

    Book save(Book book);
}