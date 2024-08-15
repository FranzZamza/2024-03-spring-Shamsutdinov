package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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

    private static List<BookDto> expectedBookDtos;

    private static final Author FIRST_AUTHOR = new Author(1, "test name");
    private static final Genre FIRST_GENRE = new Genre(1, "test name");
    private static final Book FIRST_BOOK = new Book(1, "test title", FIRST_AUTHOR, FIRST_GENRE, null);

    private final static String TITLE_MAIN = "Book List";
    private final static String TITLE_ADD_BOOK = "Add Book";
    private final static String TITLE_EDIT_BOOK = "Edit Book";


    private final static long firstBookId = 1;
    static List<Book> expectedBooks = List.of(
            FIRST_BOOK,
            new Book(2, "test title1", FIRST_AUTHOR, FIRST_GENRE, null),
            new Book(3, "test title2", FIRST_AUTHOR, FIRST_GENRE, null)
    );


    @BeforeAll
    static void beforeAll(@Autowired BookMapper bookMapper) {
        expectedBookDtos = bookMapper.toDto(expectedBooks);
    }

    @Test
    @DisplayName("должен вернуть страницу c книгами")
    void shouldReturnMainPageWithAllBooks() throws Exception {
        when(bookService.findAll()).thenReturn(expectedBookDtos);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", expectedBookDtos))
                .andExpect(content().string(containsString(TITLE_MAIN)));

        verify(bookService, times(1)).findAll();
    }

    @Test
    @DisplayName("должен вернуть форму создания книги")
    void shouldReturnBookCreateForm() throws Exception {
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TITLE_ADD_BOOK)))
                .andExpect(model().attribute("book", new BookDto()));
    }

    @Test
    @DisplayName("должен вернуть форму редактирования книни")
    void shouldReturnBookEditFormWithAuthor() throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);

        when(bookService.findById(firstBookId)).thenReturn(expectedBookDto);

        mockMvc.perform(get("/books/edit/%d".formatted(firstBookId)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TITLE_EDIT_BOOK)))
                .andExpect(model().attribute("book", expectedBookDto));

        verify(bookService, times(1)).findById(firstBookId);
    }

    @Test
    @DisplayName("должен отправить форму для сохранения книги и вернуть главную страницу")
    void shouldSaveBook() throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);

        mockMvc.perform(post("/books")
                        .param("id", String.valueOf(expectedBookDto.getId()))
                        .param("title", expectedBookDto.getTitle())
                        .param("authorName", expectedBookDto.getAuthorName())
                        .param("genreName", expectedBookDto.getGenreName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).save(any());
    }

    @Test
    @DisplayName("должен отправить форму для обновления книги и вернуть главную страницу")
    void shouldEditBook() throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);

        mockMvc.perform(post("/books/update")
                        .param("id", String.valueOf(expectedBookDto.getId()))
                        .param("title", expectedBookDto.getTitle())
                        .param("authorName", expectedBookDto.getAuthorName())
                        .param("genreName", expectedBookDto.getGenreName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).update(any());
    }

    @Test
    @DisplayName("должен удалить книгу по запросу")
    void shouldDeleteBook() throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);

        mockMvc.perform(post("/books/delete/%d".formatted(expectedBookDto.getId())))
                .andExpect(status().is3xxRedirection());

        verify(bookService, times(1)).deleteById(expectedBookDto.getId());
    }
}