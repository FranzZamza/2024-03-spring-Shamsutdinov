package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.repository.JpaAuthorRepository;
import ru.otus.hw.repository.JpaBookRepository;
import ru.otus.hw.repository.JpaGenreRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Import({BookServiceImpl.class, JpaBookRepository.class,
        JpaGenreRepository.class, JpaAuthorRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    private final static long FIRST_BOOK_ID = 1L;
    private final static long FIRST_AUTHOR_ID = 1L;
    private final static long FIRST_GENRE_ID = 1L;
    private final static String BOOK_TITLE = "Test title";
    private final static int COUNT_OF_BOOKS = 3;
    private final static String UPDATED_BOOK_TITLE = "updated test title";

    @Test
    @DisplayName("должен найти книгу по id")
    void shouldFindBookById() {
        var optionalActualBook = bookService.findById(FIRST_BOOK_ID);

        assertThat(optionalActualBook)
                .isPresent()
                .hasValueSatisfying(actualBook -> {
                    assertThat(actualBook.getId()).isEqualTo(FIRST_BOOK_ID);
                    assertThat(actualBook.getTitle()).isNotNull().isNotBlank();
                    assertThat(actualBook.getAuthor()).isNotNull();
                    assertThat(actualBook.getGenre()).isNotNull();
                });
    }

    @Test
    @DisplayName("должен найти все книги")
    void shouldFindAllBooks() {
        var actualBooks = bookService.findAll();

        assertThat(actualBooks)
                .isNotNull()
                .hasSize(COUNT_OF_BOOKS)
                .allMatch(book -> book.getId() != 0)
                .allMatch(book -> StringUtils.isNotBlank(book.getTitle()))
                .allMatch(book -> Objects.nonNull(book.getAuthor()))
                .allMatch(book -> Objects.nonNull(book.getGenre()));
    }

    @Test
    @DisplayName("должке удалить книгу")
    void shouldDeleteBook() {
        bookService.deleteById(FIRST_BOOK_ID);

        var actualBook = bookService.findById(FIRST_BOOK_ID);

        assertTrue(actualBook.isEmpty());
    }

    @Test
    @DisplayName("должен сохранить книгу и вернуть с id")
    void shouldInsertBook() {
        var actualBook = bookService.insert(BOOK_TITLE, FIRST_AUTHOR_ID, FIRST_GENRE_ID);

        assertThat(actualBook.getId()).isNotZero();
    }

    @Test
    @DisplayName("должен обновить книгу")
    void shouldUpdateBook() {
        var actualBook = bookService.update(FIRST_BOOK_ID, UPDATED_BOOK_TITLE, FIRST_AUTHOR_ID, FIRST_GENRE_ID);

        var exceptedBook = bookService.findById(FIRST_BOOK_ID).orElseThrow(()->new EntityNotFoundException("Не удалось найти обновленную сущность"));

        assertEquals(exceptedBook, actualBook);
    }
}