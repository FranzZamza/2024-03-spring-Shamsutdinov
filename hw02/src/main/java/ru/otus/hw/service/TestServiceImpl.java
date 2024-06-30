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

    private final IOService ioService;

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
        ioService.printFormattedLine("Please answer the questions below%n");
    }

    private void printQuestionWithOptions(Question question) {
        var answers = convertAnswersToText(question.answers());
        ioService.printFormattedLineWithOptions(question.text(), answers);
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
        return ioService.readIntForRangeWithPrompt(
                MIN_NUMBER_ANSWER,
                question.answers().size(),
                "\nEnter the number of the correct answer: ",
                generateErrorMessage(question)
        );
    }

    private void printMessageOfManyIncorrectAnswers() {
        ioService.printFormattedLine("you have entered too many incorrect answers, " +
                "the answer will be automatically accepted as wrong!", "\n");
    }

    private Boolean isCorrectAnswerByIndex(Question question, Integer answerIndex) {
        return question.answers().get(answerIndex - 1).isCorrect();
    }

    private String generateErrorMessage(Question question) {
        return "An incorrect answer has been entered, please select the answer from " +
                MIN_NUMBER_ANSWER +
                " to " +
                question.answers().size();
    }
}
