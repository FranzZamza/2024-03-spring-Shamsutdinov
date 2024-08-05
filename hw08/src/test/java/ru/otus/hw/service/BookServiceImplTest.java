package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.changelog.TestInitMongoDBDataChangeLog;
import ru.otus.hw.config.MongoConfig;
import ru.otus.hw.repository.BookCustomRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import({TestInitMongoDBDataChangeLog.class, MongoConfig.class,
        BookCustomRepositoryImpl.class, BookServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BookServiceImplTest {

    @Autowired
    private BookService service;

    private final static String FIRST_BOOK_ID = "1";

    private final static String BOOK_TITLE = "test title";
    private final static String FIRST_AUTHOR_ID = "1";
    private final static String FIRST_GENRE_ID = "1";

    private final static String NON_EXISTENT_ID = "100500";

    private final static String UPDATED_BOOK_TITLE = "updated book title";

    private final static int COUNT_OF_BOOKS = 3;

    @Test
    @DisplayName("должен сохранить и вернуть книгу с id")
    void shouldSaveBook() {
        var actualBook = service.insert(BOOK_TITLE, FIRST_AUTHOR_ID, FIRST_GENRE_ID);

        assertThat(actualBook)
                .isNotNull()
                .matches(book -> book.getId() != null)
                .matches(book -> book.getTitle().equals(BOOK_TITLE))
                .matches(book -> book.getAuthor().getId().equals(FIRST_AUTHOR_ID))
                .matches(book -> book.getGenre().getId().equals(FIRST_GENRE_ID));
    }

    @Test
    @DisplayName("должен выбрасить исключение на несуществующий id автора")
    void shouldThrowExceptionWhenNotFoundAuthorId() {
        assertThatThrownBy(() -> service.insert(BOOK_TITLE, NON_EXISTENT_ID, FIRST_GENRE_ID));
    }

    @Test
    @DisplayName("должен выбрасить исключение на несуществующий id жанра")
    void shouldThrowExceptionWhenNotFoundGenreId() {
        assertThatThrownBy(() -> service.insert(BOOK_TITLE, FIRST_AUTHOR_ID, NON_EXISTENT_ID));
    }

    @Test
    @DisplayName("должен обновить книгу")
    void shouldUpdateBook() {

        var actualBook = service.update(FIRST_BOOK_ID, UPDATED_BOOK_TITLE, FIRST_AUTHOR_ID, FIRST_GENRE_ID);

        assertThat(actualBook)
                .matches(book -> book.getId().equals(FIRST_BOOK_ID))
                .matches(book -> book.getTitle().equals(UPDATED_BOOK_TITLE));
    }

    @Test
    @DisplayName("должен вернуть все книги")
    void shouldReturnAllBooks() {
        var actualBooks = service.findAll();
        assertThat(actualBooks).isNotNull()
                .hasSize(COUNT_OF_BOOKS)
                .allMatch(book -> StringUtils.isNotBlank(book.getTitle()))
                .allMatch(book -> book.getAuthor() != null)
                .allMatch(book -> book.getGenre() != null);
    }

    @Test
    @DisplayName("должен удалить книгу по id")
    @DirtiesContext
    void shouldDeleteBook() {
        service.deleteById(FIRST_BOOK_ID);
        var actualBook = service.findById(FIRST_BOOK_ID);
        assertThat(actualBook).isEmpty();
    }
}
