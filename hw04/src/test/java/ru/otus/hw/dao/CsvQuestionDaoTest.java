package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.config.ApplicationConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Дао для чтения вопросов")
@SpringBootTest(classes = {ApplicationConfig.class, CsvQuestionDao.class})
class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Test
    @DisplayName("должен возврщать не пустое множество вопросов")
    void shouldReadQuestions() {
        var expectedQuestions = questionDao.findAll();
        assertFalse(expectedQuestions.isEmpty());
    }
}