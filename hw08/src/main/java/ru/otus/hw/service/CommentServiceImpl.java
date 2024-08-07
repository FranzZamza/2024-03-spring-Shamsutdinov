package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookService bookService;

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findByBookId(String id) {
        return commentRepository.findByBookId(id);
    }

    @Override
    public Comment insert(String text, String bookId) {
        throwIfBookNotFound(bookId);
        return save(null, text, bookId);
    }

    @Override
    public Comment update(String id, String text, String bookId) {
        throwIfBookNotFound(bookId);
        return save(id, text, bookId);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String text, String bookId) {
        var book = throwIfBookNotFound(bookId);

        var comment = Comment.builder()
                .id(id)
                .text(text)
                .book(book)
                .build();

        return commentRepository.save(comment);
    }

    private Book throwIfBookNotFound(String bookId) {
        return bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
    }
}

