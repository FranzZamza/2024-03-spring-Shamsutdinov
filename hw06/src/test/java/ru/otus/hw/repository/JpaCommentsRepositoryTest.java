package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaCommentsRepository.class)
@DisplayName("Репозитрий на основе JPA для работы с комментариями")
class JpaCommentsRepositoryTest {

    @Autowired
    private JpaCommentsRepository commentsRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final static long FIRST_COMMENT_ID = 1L;
    private final static long FIRST_BOOK_ID = 1L;
    private final static int COUNT_COMMENTS_OF_FIRST_BOOK = 2;
    private final static String COMMENT_TEXT = "test comment";

    @Test
    @DisplayName("должен удалить комментарий по id")
    void shouldDeleteCommentById() {
        commentsRepository.deleteById(FIRST_COMMENT_ID);
        Optional<Comment> optionalActualComment = Optional.ofNullable(entityManager.find(Comment.class, FIRST_COMMENT_ID));
        assertTrue(optionalActualComment.isEmpty());
    }

    @Test
    @DisplayName("должен сохранить комментарий и вернуть с id")
    void shouldSaveComment() {
        var book = entityManager.find(Book.class, FIRST_BOOK_ID);
        var comment = new Comment();
        comment.setText(COMMENT_TEXT);
        comment.setBook(book);

        var actualComment = commentsRepository.save(comment);
        var expectedComment = entityManager.find(Comment.class, actualComment.getId());

        assertThat(actualComment)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);

        assertThat(actualComment.getText()).isEqualTo(COMMENT_TEXT);
    }

    @Test
    @DisplayName("должен загрузить все комментарии по id книги")
    void findCommentsByBookId() {
        var comments = commentsRepository.findAllByBookId(FIRST_BOOK_ID);
        assertThat(comments)
                .isNotNull()
                .hasSize(COUNT_COMMENTS_OF_FIRST_BOOK)
                .allMatch(comment -> comment.getId() != 0)
                .allMatch(comment -> StringUtils.isNotBlank(comment.getText()))
                .allMatch(comment -> comment.getBook().getId() == FIRST_BOOK_ID);
    }

    @Test
    @DisplayName("должен загрузить информацию о комментарии по id")
    void findCommentById() {

        var optionalActualComment = commentsRepository.findById(FIRST_COMMENT_ID);
        var expectedComment = entityManager.find(Comment.class, FIRST_COMMENT_ID);

        assertThat(optionalActualComment)
                .isPresent()
                .hasValueSatisfying(actualComment -> {
                            assertThat(actualComment)
                                    .usingRecursiveComparison()
                                    .isEqualTo(expectedComment);

                            assertThat(actualComment.getBook())
                                    .isNotNull();

                            assertThat(actualComment.getId())
                                    .isNotZero();

                            assertThat(actualComment.getText()).isNotBlank();
                        }
                );
    }
}