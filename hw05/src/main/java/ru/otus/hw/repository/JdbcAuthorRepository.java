package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAll() {
        var sql = "select id, full_name from authors";
        return jdbcTemplate.query(sql, new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        var sql = "select id, full_name from authors where id=:id";
        var params = Map.of("id", id);
        return jdbcTemplate.query(sql, params, new AuthorRowMapper()).stream().findFirst();
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong("id"));
            author.setFullName(rs.getString("full_name"));
            return author;
        }
    }
}