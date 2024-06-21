package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below");
        try {
            questionDao.findAll().forEach(question -> {
                var answers = convertAnswersToText(question.answers());
                ioService.printFormattedLineWithOptions(question.text(), answers);
            });
        } catch (QuestionReadException e) {
            ioService.printLine(e.getMessage());
        }
    }

    private List<String> convertAnswersToText(List<Answer> answers) {
        return answers.stream().map(Answer::text).toList();
    }
}
