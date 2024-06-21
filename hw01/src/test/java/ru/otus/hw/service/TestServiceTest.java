package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@DisplayName("Сервис для предоставления теста")
@ExtendWith(MockitoExtension.class)
class TestServiceTest {
    @Mock
    private CsvQuestionDao csvQuestionDao;
    @Mock
    private StreamsIOService ioService;
    @InjectMocks
    private TestServiceImpl testService;

    @DisplayName("метод должен закончится QuestionReadException")
    @Test
    void shouldThrowQuestionReadException() {
        when(csvQuestionDao.findAll()).thenThrow(QuestionReadException.class);
        assertThrows(QuestionReadException.class, testService::executeTest);
    }
}