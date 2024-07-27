package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JpaBookRepository.class})
@DisplayName("Репозиторий на основе JPA работы с книгами")
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final static long FIRST_BOOK_ID = 1L;
    private final static long FIRST_GENRE_ID = 1L;
    private final static long FIRST_AUTHOR_ID = 1L;
    private final static int COUNT_OF_BOOKS = 3;

    @Test
    @DisplayName("должен загружать информацию о книге с автором и жанром по id")
    void shouldFindBookById() {

        var optionalActualBook = bookRepository.findById(FIRST_BOOK_ID);
        var expectedBook = entityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(optionalActualBook)
                .isPresent()
                .hasValueSatisfying(actualBook -> {
                            assertThat(actualBook)
                                    .usingRecursiveComparison()
                                    .isEqualTo(expectedBook);

                            assertThat(actualBook.getAuthor())
                                    .isNotNull();

                            assertThat(actualBook.getGenre())
                                    .isNotNull();

                        }
                );
    }

    @Test
    @DisplayName("должен найти всех все книги")
    void shouldFindAllAuthors() {
        var authors = bookRepository.findAll();

        assertThat(authors)
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
        bookRepository.deleteById(FIRST_BOOK_ID);
        Optional<Book> optionalActualBook = Optional.ofNullable(entityManager.find(Book.class, FIRST_BOOK_ID));
        assertTrue(optionalActualBook.isEmpty());
    }

    @Test
    @DisplayName("должен сохранить книгу и вернуть с id")
    void shouldSaveBook() {
        var genre = entityManager.find(Genre.class, FIRST_GENRE_ID);
        var author = entityManager.find(Author.class, FIRST_AUTHOR_ID);
        var book = new Book();
        book.setAuthor(author);
        book.setGenre(genre);

        var actualBook = bookRepository.save(book);
        var expectedBook = entityManager.find(Book.class, actualBook.getId());

        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

}