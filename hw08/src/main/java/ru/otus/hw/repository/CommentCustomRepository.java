package ru.otus.hw.repository;

import ru.otus.hw.model.Comment;

import java.util.List;

public interface CommentCustomRepository {
    List<Comment> findByBookId(String bookId);

    Comment insert(String text, String bookId);

    Comment update(String id, String text, String bookId);
}
