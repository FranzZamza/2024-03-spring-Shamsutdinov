package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final int MIN_NUMBER_ANSWER = 1;

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;


    @Override
    public TestResult executeTestFor(Student student) {
        printStartMessageTest();

        var questions = questionDao.findAll();

        var testResult = new TestResult(student);

        for (var question : questions) {
            printQuestionWithOptions(question);
            var isRightAnswer = evaluateAnswer(question);
            testResult.applyAnswer(question, isRightAnswer);
        }

        return testResult;
    }

    private void printStartMessageTest() {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
    }

    private void printQuestionWithOptions(Question question) {
        var answers = convertAnswersToText(question.answers());
        ioService.printTitleWithNumberedItems(question.text(), answers);
    }

    private Boolean evaluateAnswer(Question question) {
        int answerIndex;
        try {
            answerIndex = getAnsweredIndexOfQuestion(question);
        } catch (IllegalArgumentException e) {
            printMessageOfManyIncorrectAnswers();
            return false;
        }
        return isCorrectAnswerByIndex(question, answerIndex);
    }

    private List<String> convertAnswersToText(List<Answer> answers) {
        return answers.stream().map(Answer::text).toList();
    }

    private Integer getAnsweredIndexOfQuestion(Question question) throws IllegalArgumentException {
        return ioService.readIntForRangeWithPromptLocalized(
                MIN_NUMBER_ANSWER,
                question.answers().size(),
                "TestService.ask.number.answer",
                "TestService.warn.incorrect.answer"
        );
    }

    private void printMessageOfManyIncorrectAnswers() {
        ioService.printFormattedLineLocalized("TestService.too.many.incorrect.answers", "\n");
    }

    private Boolean isCorrectAnswerByIndex(Question question, Integer answerIndex) {
        return question.answers().get(answerIndex - 1).isCorrect();
    }
}
