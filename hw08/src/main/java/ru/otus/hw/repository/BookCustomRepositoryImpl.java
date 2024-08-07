package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final CommentRepository commentRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public void delete(Book book) {
        mongoTemplate.remove(book);
        var comments = commentRepository.findByBookId(book.getId());

        commentRepository.deleteAll(comments);
    }

    @Override
    public Book save(Book book) {
        mongoTemplate.save(book);
        var comments = commentRepository.findByBookId(book.getId());
        updateComments(comments, book);
        return book;
    }

    private void updateComments(List<Comment> comments, Book updatedBook) {

        if (comments.isEmpty()) {
            return;
        }
        comments.forEach(comment -> comment.setBook(updatedBook));
        commentRepository.saveAll(comments);
    }
}
