package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final MongoTemplate mongoTemplate;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Book insertWithParams(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    public Book updateWithParams(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    private Book save(String id, String title, String authorId, String genreId) {

        var author = findAuthorOrThrow(authorId);

        var genre = findGenreOrThrow(genreId);

        var book = completeBook(id, title, author, genre);

        return mongoTemplate.save(book);
    }

    private static Book completeBook(String id, String title, Author author, Genre genre) {
        var book = new Book();

        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        return book;
    }


    private Genre findGenreOrThrow(String genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
    }

    private Author findAuthorOrThrow(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
    }
}
