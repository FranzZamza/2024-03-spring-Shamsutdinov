package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Comment> findByBookId(String bookId) {
        var query = Criteria.where("book._id").is(bookId);

        var aggregation = Aggregation.newAggregation(
                Aggregation.match(query)
        );

        var result = mongoTemplate.aggregate(aggregation, "comments", Comment.class);

        return result.getMappedResults();
    }

}
