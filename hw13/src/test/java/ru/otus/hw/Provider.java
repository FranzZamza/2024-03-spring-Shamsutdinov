package ru.otus.hw;

import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.util.List;

public class Provider {
    public static final Author FIRST_AUTHOR = new Author(1, "test name");
    public static final Genre FIRST_GENRE = new Genre(1, "test name");
    public static final Book FIRST_BOOK = new Book(1, "test title", FIRST_AUTHOR, FIRST_GENRE, null);

    public final static String TITLE_MAIN = "Book List";
    public final static String TITLE_ADD_BOOK = "Add Book";
    public final static String TITLE_EDIT_BOOK = "Edit Book";

    public final static String USERNAME = "username";
    public final static String PASSWORD = "root";

    public final static long FIRST_BOOK_ID = 1;

    public static List<Book> EXPECTED_BOOKS = List.of(
            FIRST_BOOK,
            new Book(2, "test title1", FIRST_AUTHOR, FIRST_GENRE, null),
            new Book(3, "test title2", FIRST_AUTHOR, FIRST_GENRE, null)
    );
}
