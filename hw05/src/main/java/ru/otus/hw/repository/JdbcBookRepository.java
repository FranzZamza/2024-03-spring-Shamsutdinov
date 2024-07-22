package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        var sql = """
                        SELECT
                            b.id AS book_id, b.title,
                            a.id AS author_id, a.full_name,
                            g.id AS genre_id, g.name
                        FROM books b
                        INNER JOIN authors a ON b.author_id = a.id
                        INNER JOIN genres g ON b.genre_id = g.id
                        WHERE b.id = :id
                """;
        var params = Map.of("id", id);
        return jdbcTemplate.query(sql, params, new BookRowMapper()).stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        var sql = """ 
                SELECT
                    b.id as book_id, b.title,
                    a.id as author_id, a.full_name,
                    g.id as genre_id, g.name
                FROM books b
                INNER JOIN authors a  ON b.author_id = a.id
                INNER JOIN genres g ON b.genre_id = g.id
                """;
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var sql = "DELETE FROM books WHERE id = :id";
        var params = Map.of("id", id);
        jdbcTemplate.update(sql, params);
    }

    @SuppressWarnings("DataFlowIssue")
    private Book insert(Book book) {
        var sql = """
                INSERT INTO books (title, author_id, genre_id)
                VALUES (:title, :authorId, :genreId)""";

        var keyHolder = new GeneratedKeyHolder();

        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());
        params.addValue("genreId", book.getGenre().getId());

        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        book.setId(keyHolder.getKeyAs(Long.class));

        return book;
    }

    private Book update(Book book) {
        var sql = """
                UPDATE books
                SET title = :title,
                    author_id = :authorId,
                    genre_id = :genreId
                WHERE id = :id""";

        var params = Map.of("id", book.getId(),
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId(),
                "genreId", book.getGenre().getId());

        int updatedRow = jdbcTemplate.update(sql, params);
        throwIfNotUpdated(updatedRow, book);
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("book_id"));
            book.setTitle(rs.getString("title"));

            Author author = new Author();
            author.setId(rs.getLong("author_id"));
            author.setFullName(rs.getString("full_name"));
            book.setAuthor(author);

            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("name"));
            book.setGenre(genre);

            return book;
        }
    }

    private void throwIfNotUpdated(int updatedRow, Book book) {
        if (updatedRow == 0) {
            throw new EntityNotFoundException("Failed to update book with ID " + book.getId() + ": Entity not found.");
        }
    }
}