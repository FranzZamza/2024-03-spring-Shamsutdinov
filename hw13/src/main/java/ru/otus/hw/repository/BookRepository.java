package ru.otus.hw.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "books_entity_graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> findByTitle(@NonNull String title);

    @NonNull
    @EntityGraph(value = "books_entity_graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAll();

    @NonNull
    @EntityGraph(value = "books_entity_graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> findById(@NonNull Long id);
}