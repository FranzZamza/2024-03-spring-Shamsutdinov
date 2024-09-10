package ru.otus.hw.hw14.config.step.cash;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthorCash implements Cash {
    private final Map<String, String> cash = new HashMap<>();

    public void add(String fullName, String id) {
        cash.put(fullName, id);
    }

    public String getId(String fullName) {
        return cash.get(fullName);
    }
}
