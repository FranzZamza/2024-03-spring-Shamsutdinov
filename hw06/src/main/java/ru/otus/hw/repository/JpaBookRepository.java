package ru.otus.hw.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        var entityGraph = em.getEntityGraph("books_entity_graph");

        return Optional.ofNullable(em.find(Book.class, id, Map.of("javax.persistence.fetchgraph", entityGraph)));
    }

    @Override
    public List<Book> findAll() {
        var entityGraph = em.getEntityGraph("books_entity_graph");

        return em.createQuery("SELECT b FROM Book b", Book.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public Book save(Book book) {
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Book.class, id));
    }
}
