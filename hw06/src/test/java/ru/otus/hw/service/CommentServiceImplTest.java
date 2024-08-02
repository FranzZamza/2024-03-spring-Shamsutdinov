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
import ru.otus.hw.repository.JpaCommentsRepository;
import ru.otus.hw.repository.JpaGenreRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("Сервис для работы с комментариями")
@Import({BookServiceImpl.class, JpaBookRepository.class,
        JpaCommentsRepository.class, JpaAuthorRepository.class,
        JpaGenreRepository.class, CommentServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    private final static long FIRST_COMMENT_ID = 1L;
    private final static long FIRST_BOOK_ID = 1L;
    private final static int COUNT_OF_COMMENTS_OF_FIRST_BOOK = 2;
    private final static String COMMENT_TEXT = "test comment";
    private final static String UPDATED_COMMENT_TEXT = "tesT comment updated";

    @Test
    @DisplayName("должен найти комментарий по id")
    void shouldFindCommentById() {
        var optionalActualComment = commentService.findById(1L);
        assertThat(optionalActualComment)
                .isPresent()
                .hasValueSatisfying(actualComment -> {
                    assertThat(actualComment.getId()).isEqualTo(FIRST_COMMENT_ID);
                    assertThat(actualComment.getText()).isNotNull().isNotBlank();
                    assertThat(actualComment.getBook()).isNotNull();
                });
    }

    @Test
    @DisplayName("должен найти комментарий по id книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldFindCommentsByBookId() {
        var actualComments = commentService.findByBookId(FIRST_BOOK_ID);
        assertThat(actualComments)
                .isNotNull()
                .hasSize(COUNT_OF_COMMENTS_OF_FIRST_BOOK)
                .allMatch(comment -> comment.getId() != 0)
                .allMatch(comment -> StringUtils.isNotBlank(comment.getText()))
                .allMatch(comment -> Objects.nonNull(comment.getBook()));
    }

    @Test
    @DisplayName("должен удалить комментарий по id")
    void shouldDeleteCommentById() {
        commentService.deleteById(FIRST_COMMENT_ID);
        var optionalActualComments = commentService.findById(FIRST_COMMENT_ID);
        assertTrue(optionalActualComments.isEmpty());
    }

    @Test
    @DisplayName("должен сохранить комментарий и вернуть с id")
    void shouldSaveComment() {
        var actualComment = commentService.insert(COMMENT_TEXT, FIRST_BOOK_ID);
        assertThat(actualComment.getId()).isNotZero();
    }

    @Test
    @DisplayName("должен обновить комменатарий")
    void shouldUpdateComment() {
        var actualComment = commentService.update(FIRST_COMMENT_ID, UPDATED_COMMENT_TEXT, FIRST_BOOK_ID);
        var expectedComment = commentService.findById(FIRST_COMMENT_ID).orElseThrow(() -> new EntityNotFoundException("Не найден обновленный комментарий!"));
        assertThat(actualComment).isEqualTo(expectedComment);
    }

}