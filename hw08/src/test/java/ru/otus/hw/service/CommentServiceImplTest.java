package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.changelog.TestInitMongoDBDataChangeLog;
import ru.otus.hw.config.MongoConfig;
import ru.otus.hw.repository.CommentCustomRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import({CommentServiceImpl.class, TestInitMongoDBDataChangeLog.class,
        MongoConfig.class, CommentCustomRepositoryImpl.class,
        BookServiceImpl.class, GenreServiceImpl.class,
        AuthorServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;


    private final static String FIRST_BOOK_ID = "1";

    private final static String TEXT = "test title";
    private final static String FIRST_COMMENT_ID = "1";

    private final static String NON_EXISTENT_ID = "100500";

    private final static String UPDATED_TEXT = "updated text";

    private final static int COUNT_OF_COMMENTS_BY_BOOK_ID = 3;

    @Test
    @DisplayName("должен сохранить и вернуть комментарий с id")
    void shouldSaveComment() {
        var actualComment = commentService.insert(TEXT, FIRST_BOOK_ID);

        assertThat(actualComment)
                .isNotNull()
                .matches(comment -> comment.getId() != null)
                .matches(comment -> comment.getText().equals(TEXT));
    }

    @Test
    @DisplayName("должен выбрасить исключение на несуществующий id автора")
    void shouldThrowExceptionWhenNotFoundBookId() {
        assertThatThrownBy(() -> commentService.insert(TEXT, NON_EXISTENT_ID));
    }


    @Test
    @DisplayName("должен обновить комментарий")
    void shouldUpdateComment() {

        var actualComment = commentService.update(FIRST_COMMENT_ID, UPDATED_TEXT, FIRST_BOOK_ID);

        assertThat(actualComment)
                .matches(comment -> comment.getId().equals(FIRST_COMMENT_ID))
                .matches(comment -> comment.getBook().getId().equals(FIRST_BOOK_ID))
                .matches(comment -> comment.getText().equals(UPDATED_TEXT));
    }

    @Test
    @DisplayName("должен вернуть все комментарии по книге")
    void shouldReturnAllCommentsByBook() {
        var actualComments = commentService.findByBookId(FIRST_BOOK_ID);
        assertThat(actualComments).isNotNull()
                .hasSize(COUNT_OF_COMMENTS_BY_BOOK_ID)
                .allMatch(comment -> comment.getBook().getId().equals(FIRST_BOOK_ID));
    }

    @Test
    @DisplayName("должен удалить комментарий по id")
    @DirtiesContext
    void shouldDeleteBook() {
        commentService.deleteById(FIRST_COMMENT_ID);
        var actualComment = commentService.findById(FIRST_BOOK_ID);
        assertThat(actualComment).isEmpty();
    }

    @Test
    @DisplayName("должен найти комментарий по id")
    void shouldFindCommentById() {
        var actualComment = commentService.findById(FIRST_COMMENT_ID);
        assertThat(actualComment).isNotNull()
                .hasValueSatisfying(comment -> assertThat(comment.getId()).isEqualTo(FIRST_COMMENT_ID));
    }
}