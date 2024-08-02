package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaGenreRepository.class})
@DisplayName("Репозиторий на основке JPA для работы с жанрам")
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final static long FIRST_GENRE_ID = 1L;
    private final static int COUNT_OF_GENRE = 3;

    @Test
    @DisplayName("должен загрузить информациб о жанре по id ")
    void shouldFindGenreById() {
        var optionalActualGenre = genreRepository.findById(FIRST_GENRE_ID);
        var exceptedGenre = entityManager.find(Genre.class, FIRST_GENRE_ID);

        assertThat(optionalActualGenre)
                .isPresent()
                .hasValueSatisfying(actualAuthor ->
                        assertThat(actualAuthor)
                                .usingRecursiveComparison()
                                .isEqualTo(exceptedGenre));
    }

    @Test
    @DisplayName("должен найти все жанры")
    void shouldFindAllAuthors() {
        var authors = genreRepository.findAll();
        assertThat(authors)
                .isNotNull()
                .hasSize(COUNT_OF_GENRE)
                .allMatch(genre -> StringUtils.isNotBlank(genre.getName()))
                .allMatch(genre -> genre.getId() != 0);
    }

}