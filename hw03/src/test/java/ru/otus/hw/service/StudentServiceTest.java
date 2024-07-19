package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@DisplayName("Сервис для определения студента")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private LocalizedIOService ioService;

    private StudentService studentService;

    private static final String STUDENT_TEST_NAME = "testName";

    @BeforeEach
    void setUp() {
        given(ioService.readStringWithPromptLocalized(anyString())).willReturn(STUDENT_TEST_NAME);
        studentService = new StudentServiceImpl(ioService);
    }

    @Test
    @DisplayName("Создать студента с именем и фамилией полученные от ioService")
    void shouldCreateStudent() {
        var expectedStudent = studentService.determineCurrentStudent();
        assertAll(
                () -> assertEquals(STUDENT_TEST_NAME, expectedStudent.firstName()),
                () -> assertEquals(STUDENT_TEST_NAME, expectedStudent.lastName()));
    }

}