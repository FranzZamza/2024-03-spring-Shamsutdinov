package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    private final static Long FIRST_BOOK_ID = 1L;
    private final static int COUNT_COMMENTS_OF_FIRST_BOOK = 2;

    @Test
    @DisplayName("должен найти комментарии по id книги")
    void shouldFindByBookId() {
        var actualComments = commentRepository.findAllByBookId(FIRST_BOOK_ID);
        assertThat(actualComments)
                .isNotNull()
                .hasSize(COUNT_COMMENTS_OF_FIRST_BOOK)
                .allMatch(comment -> comment.getId() != 0)
                .allMatch(comment -> StringUtils.isNotBlank(comment.getText()))
                .allMatch(comment -> Objects.equals(comment.getBook().getId(), FIRST_BOOK_ID));

    }
}