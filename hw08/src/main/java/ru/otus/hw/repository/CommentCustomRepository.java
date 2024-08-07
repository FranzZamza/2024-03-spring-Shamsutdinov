package ru.otus.hw.repository;

import ru.otus.hw.model.Comment;

import java.util.List;

public interface CommentCustomRepository {
    List<Comment> findByBookId(String bookId);
}
