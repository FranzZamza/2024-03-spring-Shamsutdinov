package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/books")
    String getBooks(Model model) {
        var bookDtos = bookService.findAll();
        model.addAttribute("books", bookDtos);
        return "book/main";
    }

    @GetMapping("/books/edit/{id}")
    String getEditForm(@PathVariable("id") long id, Model model) {
        var bookDto = bookService.findById(id);
        var genresDtos = genreService.findAll();
        var authorsDtos = authorService.findAll();

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genresDtos);
        model.addAttribute("authors", authorsDtos);

        return "book/bookForm";
    }

    @GetMapping("/books/new")
    String getCreateForm(Model model) {
        var genreDtos = genreService.findAll();
        var authorDtos = authorService.findAll();

        model.addAttribute("genres", genreDtos);
        model.addAttribute("authors", authorDtos);
        model.addAttribute("book", new BookDto());

        return "book/bookForm";
    }

    @PostMapping("/books")
    String saveBook(@ModelAttribute BookDto bookDto) {
        bookService.save(bookDto);
        return "redirect:/books";
    }

    @PostMapping("/books/update")
    String updateBook(@ModelAttribute BookDto bookDto) {
        bookService.update(bookDto);
        return "redirect:/books";
    }

    @PostMapping("/books/delete/{id}")
    String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
