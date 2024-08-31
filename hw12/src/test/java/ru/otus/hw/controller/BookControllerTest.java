package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mapper.AuthorMapperImpl;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.BookMapperImpl;
import ru.otus.hw.mapper.GenreMapperImpl;
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
import static ru.otus.hw.Provider.EXPECTED_BOOKS;
import static ru.otus.hw.Provider.FIRST_BOOK_ID;
import static ru.otus.hw.Provider.PASSWORD;
import static ru.otus.hw.Provider.TITLE_ADD_BOOK;
import static ru.otus.hw.Provider.TITLE_EDIT_BOOK;
import static ru.otus.hw.Provider.TITLE_MAIN;
import static ru.otus.hw.Provider.USERNAME;

@WebMvcTest(BookController.class)
@Import({BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class, SecurityConfig.class})
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

    @BeforeAll
    static void beforeAll(@Autowired BookMapper bookMapper) {
        expectedBookDtos = bookMapper.toDto(EXPECTED_BOOKS);
    }

    @Test
    @DisplayName("должен вернуть страницу c книгами")
    @WithMockUser(username = USERNAME, password = PASSWORD)
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
    @WithMockUser(username = "user", password = "root")
    void shouldReturnBookCreateForm() throws Exception {
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TITLE_ADD_BOOK)))
                .andExpect(model().attribute("book", new BookDto()));
    }

    @Test
    @DisplayName("должен вернуть форму редактирования книни")
    @WithMockUser(username = "user", password = "root")
    void shouldReturnBookEditFormWithAuthor() throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);

        when(bookService.findById(FIRST_BOOK_ID)).thenReturn(expectedBookDto);

        mockMvc.perform(get("/books/edit/%d".formatted(FIRST_BOOK_ID)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TITLE_EDIT_BOOK)))
                .andExpect(model().attribute("book", expectedBookDto));

        verify(bookService, times(1)).findById(FIRST_BOOK_ID);
    }

    @Test
    @DisplayName("должен отправить форму для сохранения книги и вернуть главную страницу")
    @WithMockUser(username = USERNAME, password = PASSWORD)
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
    @WithMockUser(username = "user", password = "root")
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
    @WithMockUser(username = "user", password = "root")
    void shouldDeleteBook() throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);

        mockMvc.perform(post("/books/delete/%d".formatted(expectedBookDto.getId())))
                .andExpect(status().is3xxRedirection());

        verify(bookService, times(1)).deleteById(expectedBookDto.getId());
    }

}