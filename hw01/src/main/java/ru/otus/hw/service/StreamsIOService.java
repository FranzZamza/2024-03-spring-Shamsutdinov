package ru.otus.hw.service;

import java.io.PrintStream;
import java.util.List;

public class StreamsIOService implements IOService {
    private final PrintStream printStream;

    public StreamsIOService(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public void printFormattedLineWithOptions(String question, List<String> answers) {
        printStream.printf("%n" + question + "%n");
        answers.forEach(answer -> {
            printStream.printf("%4d. %s%n", answers.indexOf(answer) + 1, answer);
        });
    }
}
