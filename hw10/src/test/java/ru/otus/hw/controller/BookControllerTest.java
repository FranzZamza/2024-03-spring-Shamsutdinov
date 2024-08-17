package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mapper.AuthorMapperImpl;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.BookMapperImpl;
import ru.otus.hw.mapper.GenreMapperImpl;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import({BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @Autowired
    ObjectMapper objectMapper;

    private static List<BookDto> expectedBookDtos;

    private static final Author FIRST_AUTHOR = new Author(1, "test name");
    private static final Genre FIRST_GENRE = new Genre(1, "test name");
    private static final Book FIRST_BOOK = new Book(1, "test title", FIRST_AUTHOR, FIRST_GENRE, null);

    private static final List<Book> EXPECTED_BOOKS = List.of(
            FIRST_BOOK,
            new Book(2, "test title1", FIRST_AUTHOR, FIRST_GENRE, null),
            new Book(3, "test title2", FIRST_AUTHOR, FIRST_GENRE, null)
    );

    @BeforeAll
    static void beforeAll(@Autowired BookMapper bookMapper) {
        expectedBookDtos = bookMapper.toDto(EXPECTED_BOOKS);
    }

    @Test
    @DisplayName("должен вернуть дто книг")
    void shouldReturnBooks() throws Exception {
        when(bookService.findAll()).thenReturn(expectedBookDtos);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBookDtos)));

        verify(bookService, times(1)).findAll();
    }

    @Test
    @DisplayName("должен сохранить книгу")
    void shouldReturnBookCreateForm() throws Exception {
        var firstBookDto = expectedBookDtos.get(0);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstBookDto)))
                .andExpect(status().is2xxSuccessful());

        verify(bookService, times(1)).save(any());
    }

    @Test
    @DisplayName("должен вернуть книгу по id")
    void shouldReturnBookById() throws Exception {
        var firstBookDto = expectedBookDtos.get(0);

        when(bookService.findById(anyLong())).thenReturn(firstBookDto);

        mockMvc.perform(get("/api/v1/books/" + firstBookDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(firstBookDto)));

        verify(bookService, times(1)).findById(firstBookDto.getId());
    }

    @Test
    @DisplayName("должен обновить книгу по id")
    void shouldReturnBookEditForm() throws Exception {
        var firstBookDto = expectedBookDtos.get(0);

        mockMvc.perform(put("/api/v1/books/" + firstBookDto.getId())
                        .content(objectMapper.writeValueAsString(firstBookDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(bookService, times(1)).update(any());
    }

    @Test
    @DisplayName("должен удалить книгу по id")
    void shouldDeleteBookById() throws Exception {
        var firstBookDto = expectedBookDtos.get(0);

        mockMvc.perform(delete("/api/v1/books/" + firstBookDto.getId()))
                .andExpect(status().is2xxSuccessful());

        verify(bookService, times(1)).deleteById(firstBookDto.getId());
    }

}