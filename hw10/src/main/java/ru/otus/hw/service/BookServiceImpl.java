package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
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

    private final BookMapper bookMapper;


    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return bookMapper.toDto(books);
    }

    @Override
    @Transactional
    public void save(BookDto bookDto) {
        var book = bookMapper.toBook(bookDto);

        var author = findAuthorByNameOrThrow(book.getAuthor().getFullName());
        var genre = findGenreByNameOrThrow(book.getGenre().getName());

        book.setAuthor(author);
        book.setGenre(genre);

        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void update(BookDto bookDto) {
        var book = bookMapper.toBook(bookDto);

        var sourceBook = findBookByIdOrThrow(book.getId());

        var author = findAuthorByNameOrThrow(book.getAuthor().getFullName());
        var genre = findGenreByNameOrThrow(book.getGenre().getName());

        sourceBook.setTitle(book.getTitle());
        sourceBook.setAuthor(author);
        sourceBook.setGenre(genre);

        bookRepository.save(sourceBook);
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
