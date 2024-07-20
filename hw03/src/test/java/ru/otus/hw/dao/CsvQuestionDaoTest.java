package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Дао для чтения вопросов")
class CsvQuestionDaoTest {
    private QuestionDao questionDao;
    private final static Integer RIGHT_ANSWERS_TO_PASS = 3;
    private final static String TEST_FILE_NAME = "test-questions_ru.csv";
    private final static String LOCAL = "ru-RU";

    @BeforeEach
    void setup() {
        AppProperties provider = new AppProperties();
        provider.setRightAnswersCountToPass(RIGHT_ANSWERS_TO_PASS);
        provider.setLocale(LOCAL);
        provider.setFileNameByLocaleTag(Map.of(LOCAL, TEST_FILE_NAME));
        questionDao = new CsvQuestionDao(provider);
    }

    @Test
    @DisplayName("должен возврщать не пустое множество вопросов")
    void shouldReadQuestions() {
        var expectedQuestions = questionDao.findAll();
        assertFalse(expectedQuestions.isEmpty());
    }
}