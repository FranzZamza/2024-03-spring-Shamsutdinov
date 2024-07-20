package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для определения студента")
@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private LocalizedIOService ioService;

    private static final String STUDENT_TEST_NAME = "testName";

    @Test
    @DisplayName("Создать студента с именем и фамилией полученные от ioService")
    void shouldCreateStudent() {
        when(ioService.readStringWithPromptLocalized(anyString())).thenReturn(STUDENT_TEST_NAME);

        var expectedStudent = studentService.determineCurrentStudent();
        assertAll(
                () -> assertEquals(STUDENT_TEST_NAME, expectedStudent.firstName()),
                () -> assertEquals(STUDENT_TEST_NAME, expectedStudent.lastName()));
    }
}