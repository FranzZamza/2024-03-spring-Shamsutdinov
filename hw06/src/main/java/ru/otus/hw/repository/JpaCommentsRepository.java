package ru.otus.hw.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentsRepository implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        var entityGraph = em.getEntityGraph("comment_entity_graph");
        return em.createQuery("SELECT c FROM Comment c WHERE c.book.id = :bookId", Comment.class)
                .setParameter("bookId", bookId)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Comment.class, id));
    }


}
