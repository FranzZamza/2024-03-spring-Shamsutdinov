package ru.otus.hw.repository;

import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long bookId);

    Comment save(Comment comment);

    void deleteById(long id);
}
