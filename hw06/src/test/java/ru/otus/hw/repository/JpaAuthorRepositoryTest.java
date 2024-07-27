package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.model.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuthorRepository.class})
@DisplayName("Репозиторий на основе JPA для работы с авторами")
class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final static long FIRST_AUTHOR_ID = 1L;
    private final static int COUNT_OF_AUTHORS = 3;

    @Test
    @DisplayName("должен загружать информацию о авторе по id")
    void shouldFindAuthorById() {
        var optionalActualAuthor = authorRepository.findById(FIRST_AUTHOR_ID);
        var expectedAuthor = entityManager.find(Author.class, FIRST_AUTHOR_ID);

        assertThat(optionalActualAuthor)
                .isPresent()
                .hasValueSatisfying(actualAuthor ->
                        assertThat(actualAuthor)
                                .usingRecursiveComparison()
                                .isEqualTo(expectedAuthor)
                );
    }

    @Test
    @DisplayName("должен найти всех авторов")
    void shouldFindAllAuthors() {
        var authors = authorRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(COUNT_OF_AUTHORS)
                .allMatch(author -> StringUtils.isNotBlank(author.getFullName()))
                .allMatch(author -> author.getId() != 0);
    }
}