package ru.otus.hw.repository;

import ru.otus.hw.model.Book;

public interface BookCustomRepository {

    Book insertWithParams(String title, String authorId, String genreId);

    Book updateWithParams(String id, String title, String authorId, String genreId);
}
