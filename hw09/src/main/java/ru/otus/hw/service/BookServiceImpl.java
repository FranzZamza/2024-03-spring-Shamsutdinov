package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book save(Book book) {
        var author = findAuthorByNameOrThrow(book.getAuthor().getFullName());
        var genre = findGenreByNameOrThrow(book.getGenre().getName());

        book.setAuthor(author);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Book book) {
        var sourceBook = findBookByIdOrThrow(book.getId());

        var author = findAuthorByNameOrThrow(book.getAuthor().getFullName());
        var genre = findGenreByNameOrThrow(book.getGenre().getName());

        sourceBook.setTitle(book.getTitle());
        sourceBook.setAuthor(author);
        sourceBook.setGenre(genre);

        return bookRepository.save(sourceBook);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        var book = findBookByIdOrThrow(id);
        bookRepository.delete(book);
    }

    private Book findBookByIdOrThrow(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    private Author findAuthorByNameOrThrow(String fullName) {
        return authorRepository.findAuthorByFullName(fullName)
                .orElseThrow(() -> new EntityNotFoundException("Author with full name %s not found"
                        .formatted(fullName)));
    }

    private Genre findGenreByNameOrThrow(String name) {
        return genreRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Genre with name %s not found"
                        .formatted(name)));
    }
}
