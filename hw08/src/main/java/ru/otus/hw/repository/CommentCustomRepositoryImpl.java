package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final MongoTemplate mongoTemplate;

    private final BookRepository bookRepository;

    @Override
    public List<Comment> findByBookId(String bookId) {
        var query = Criteria.where("book._id").is(bookId);

        var aggregation = Aggregation.newAggregation(
                Aggregation.match(query)
        );

        var result = mongoTemplate.aggregate(aggregation, "comments", Comment.class);

        return result.getMappedResults();
    }

    @Override
    public Comment insert(String text, String bookId) {
        return save(null, text, bookId);
    }

    @Override
    public Comment update(String id, String text, String bookId) {
        return save(id, text, bookId);
    }

    private Comment save(String id, String text, String bookId) {
        var book = findBookOrThrow(bookId);

        var comment = new Comment();
        comment.setId(id);
        comment.setText(text);
        comment.setBook(book);

        return mongoTemplate.save(comment);
    }

    private Book findBookOrThrow(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
    }
}
