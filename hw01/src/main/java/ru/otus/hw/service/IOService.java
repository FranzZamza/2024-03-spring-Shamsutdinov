package ru.otus.hw.service;

import java.util.List;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object... args);

    void printFormattedLineWithOptions(String question, List<String> answers);
}
