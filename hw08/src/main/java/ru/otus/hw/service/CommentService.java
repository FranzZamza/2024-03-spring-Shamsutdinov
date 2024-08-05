package ru.otus.hw.service;

import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(String id);

    List<Comment> findByBookId(String id);

    Comment insert(String text, String bookId);

    Comment update(String id, String text, String bookId);

    void deleteById(String id);
}
