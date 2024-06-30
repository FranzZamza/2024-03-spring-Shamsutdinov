package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Дао для чтения вопросов")
class CsvQuestionDaoTest {
    private QuestionDao questionDao;
    private final static Integer RIGHT_ANSWERS_TO_PASS = 3;
    private final static String TEST_FILE_NAME = "test-questions.csv";

    @BeforeEach
    void setup() {
        AppProperties provider = new AppProperties(RIGHT_ANSWERS_TO_PASS, TEST_FILE_NAME);
        questionDao = new CsvQuestionDao(provider);
    }

    @Test
    @DisplayName("должен возврщать не пустое множество вопросов")
    void shouldReadQuestions() {
        var expectedQuestions = questionDao.findAll();
        assertFalse(expectedQuestions.isEmpty());
    }

}