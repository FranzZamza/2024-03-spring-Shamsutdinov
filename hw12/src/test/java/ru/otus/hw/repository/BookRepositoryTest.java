package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private final static int COUNT_OF_BOOKS = 3;
    private final static Long FIRST_BOOK_ID = 1L;

    @Test
    @DisplayName("должен найти книгу по id и выгрузить автора с жанром")
    void shouldFindBookById() {
        var optionalActualBook = bookRepository.findById(FIRST_BOOK_ID);
        assertThat(optionalActualBook)
                .isPresent()
                .hasValueSatisfying(actualBook -> {
                            assertThat(actualBook.getTitle())
                                    .isNotNull()
                                    .isNotBlank();

                            assertThat(actualBook.getAuthor())
                                    .isNotNull();

                            assertThat(actualBook.getGenre())
                                    .isNotNull();

                        }
                );
    }

    @Test
    @DisplayName("должен найти все книги и выгрузить авторов с жанрами")
    void shouldFindAll() {
        var actualBooks = bookRepository.findAll();

        assertThat(actualBooks)
                .isNotNull()
                .hasSize(COUNT_OF_BOOKS)
                .allMatch(book -> book.getId() != 0)
                .allMatch(book -> StringUtils.isNotBlank(book.getTitle()))
                .allMatch(book -> Objects.nonNull(book.getAuthor()))
                .allMatch(book -> Objects.nonNull(book.getGenre()));
    }

}