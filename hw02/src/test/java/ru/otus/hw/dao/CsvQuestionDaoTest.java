package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;
import ru.otus.hw.config.ApplicationTestConfig;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Дао для чтения вопросов")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class, ApplicationTestConfig.class})
class CsvQuestionDaoTest {
    @Autowired
    QuestionDao questionDao;

    @Test
    @DisplayName("должен возврщать не пустое множество вопросов")
    void shouldReadQuestions() {
        var expectedQuestions = questionDao.findAll();
        assertFalse(expectedQuestions.isEmpty());
    }

}