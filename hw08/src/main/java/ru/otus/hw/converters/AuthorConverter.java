package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.model.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}