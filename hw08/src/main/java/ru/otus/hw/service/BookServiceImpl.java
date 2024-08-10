package ru.otus.hw.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final GenreService genreService;

    private final AuthorService authorService;


    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    public Book update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    public void deleteById(String id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        bookRepository.delete(book);
    }

    private Book save(String id, String title, String authorId, String genreId) {

        var author = authorService.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));

        var genre = genreService.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(id)));

        var book = Book.builder()
                .id(id)
                .title(title)
                .author(author)
                .genre(genre)
                .build();

        return bookRepository.save(book);
    }

}
