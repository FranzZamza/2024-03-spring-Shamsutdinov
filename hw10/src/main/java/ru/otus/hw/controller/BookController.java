package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.service.BookService;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("api/v1/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("api/v1/books/{id}")
    public BookDto getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PostMapping("api/v1/books")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestBody BookDto bookDto) {
        bookService.save(bookDto);
    }

    @PutMapping("api/v1/books/{id}")
    public void updateBook(@PathVariable long id, @RequestBody BookDto bookDto) {
        throwIfIdsNotEquals(id, bookDto);
        bookService.update(bookDto);
    }

    @DeleteMapping("api/v1/books/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }

    private void throwIfIdsNotEquals(long id, BookDto bookDto) {
        if (isNotEqualIds(id, bookDto)) {
            throw new IllegalArgumentException("book id must be equal to id");
        }
    }

    private boolean isNotEqualIds(long id, BookDto bookDto) {
        return !Objects.equals(id, bookDto.getId());
    }
}
