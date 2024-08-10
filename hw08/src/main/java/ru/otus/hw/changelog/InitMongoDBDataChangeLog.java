package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;
import ru.otus.hw.repository.GenreRepository;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author firstAuthor;

    private Author secondAuthor;

    private Author thirdAuthor;

    private Genre firstGenre;

    private Genre secondGenre;

    private Genre thirdGenre;

    private Book firstBook;

    private Book secondBook;


    @ChangeSet(order = "000", id = "dropDB", author = "Artem Shamsutdinov", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();

        database.createCollection("authors");
        database.createCollection("comments");
        database.createCollection("genres");
    }

    @ChangeSet(order = "001", id = "initAuthor", author = "Artem Shamsutdinov", runAlways = true)
    public void initAuthor(AuthorRepository repository) {
        firstAuthor = repository.save(new Author("1", "Author_1"));
        secondAuthor = repository.save(new Author("2", "Author_2"));
        thirdAuthor = repository.save(new Author("3", "Author_3"));
    }

    @ChangeSet(order = "002", id = "initGenre", author = "Artem Shamsutdinov", runAlways = true)
    public void initGenre(GenreRepository repository) {
        firstGenre = repository.save(new Genre("1", "Genre_1"));
        secondGenre = repository.save(new Genre("2", "Genre_2"));
        thirdGenre = repository.save(new Genre("3", "Genre_3"));
    }

    @ChangeSet(order = "003", id = "initBook", author = "Artem Shamsutdinov", runAlways = true)
    public void initBook(BookRepository repository) {
        firstBook = repository.save(new Book("1", "Title_1", firstAuthor, firstGenre, null));
        secondBook = repository.save(new Book("2", "Title_2", secondAuthor, secondGenre, null));
        repository.save(new Book("3", "Title_3", thirdAuthor, thirdGenre, null));
    }


    @ChangeSet(order = "004", id = "initComments", author = "Artem Shamsutdinov", runAlways = true)
    public void initGenre(CommentRepository repository) {
        repository.save(new Comment("1", "good1", firstBook));
        repository.save(new Comment("3", "good3", firstBook));
        repository.save(new Comment("4", "good4", firstBook));
        repository.save(new Comment("2", "good2", firstBook));
        repository.save(new Comment("3", "good3", secondBook));
    }
}
