package ru.otus.hw.repository.utils;

public enum Hint {
    FETCH_GRAPH("javax.persistence.fetchgraph");

    private final String propertyName;

    Hint(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
