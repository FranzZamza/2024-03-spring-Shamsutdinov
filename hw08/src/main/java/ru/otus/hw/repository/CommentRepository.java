package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String>, CommentCustomRepository {
}

