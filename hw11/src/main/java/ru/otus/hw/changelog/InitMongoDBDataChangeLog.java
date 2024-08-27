package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author firstAuthor;

    private Author secondAuthor;

    private Author thirdAuthor;

    private Genre firstGenre;

    private Genre secondGenre;

    private Genre thirdGenre;

    @ChangeSet(order = "000", id = "dropDB", author = "Artem Shamsutdinov", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();

        database.createCollection("authors");
        database.createCollection("comments");
        database.createCollection("genres");
    }

    @ChangeSet(order = "001", id = "initAuthor", author = "Artem Shamsutdinov", runAlways = true)
    public void initAuthor(MongockTemplate template) {
        firstAuthor = template.save(new Author("1", "Author_1"));
        secondAuthor = template.save(new Author("2", "Author_2"));
        thirdAuthor = template.save(new Author("3", "Author_3"));
    }

    @ChangeSet(order = "002", id = "initGenre", author = "Artem Shamsutdinov", runAlways = true)
    public void initGenre(MongockTemplate template) {
        firstGenre = template.save(new Genre("1", "Genre_1"));
        secondGenre = template.save(new Genre("2", "Genre_2"));
        thirdGenre = template.save(new Genre("3", "Genre_3"));
    }

    @ChangeSet(order = "003", id = "initBook", author = "Artem Shamsutdinov", runAlways = true)
    public void initBook(MongockTemplate template) {
        template.save(new Book("1", "Title_1", firstAuthor, firstGenre));
        template.save(new Book("2", "Title_2", secondAuthor, secondGenre));
        template.save(new Book("3", "Title_3", thirdAuthor, thirdGenre));
    }
}
