package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    @GetMapping("/books")
    String getBooks(Model model) {
        var books = bookService.findAll();
        var dtos = bookMapper.toDto(books);
        model.addAttribute("books", dtos);
        return "book/main";
    }

    @GetMapping("/books/edit/{id}")
    String getEditForm(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);
        var genres = genreService.findAll();
        var authors = authorService.findAll();

        var bookDto = bookMapper.toDto(book);
        var genresDtos = genreMapper.toDto(genres);
        var authorsDtos = authorMapper.toDto(authors);

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genresDtos);
        model.addAttribute("authors", authorsDtos);

        return "book/bookForm";
    }

    @GetMapping("/books/new")
    String getCreateForm(Model model) {
        var genres = genreService.findAll();
        var authors = authorService.findAll();

        var genresDtos = genreMapper.toDto(genres);
        var authorsDtos = authorMapper.toDto(authors);

        model.addAttribute("genres", genresDtos);
        model.addAttribute("authors", authorsDtos);
        model.addAttribute("book", new BookDto());

        return "book/bookForm";
    }

    @PostMapping("/books")
    String saveBook(@ModelAttribute BookDto dto) {
        var book = bookMapper.toBook(dto);
        bookService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/books/update")
    String updateBook(@ModelAttribute BookDto dto) {
        var book = bookMapper.toBook(dto);
        bookService.update(book);
        return "redirect:/books";
    }

    @PostMapping("/books/delete/{id}")
    String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
