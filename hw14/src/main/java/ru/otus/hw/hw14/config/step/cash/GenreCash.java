package ru.otus.hw.hw14.config.step.cash;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GenreCash implements Cash {
    private final Map<String, String> cash = new HashMap<>();

    public void add(String name, String id) {
        cash.put(name, id);
    }

    public String getId(String name) {
        return cash.get(name);
    }
}
