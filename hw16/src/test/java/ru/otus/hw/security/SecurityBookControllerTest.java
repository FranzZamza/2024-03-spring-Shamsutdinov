package ru.otus.hw.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mapper.AuthorMapperImpl;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.BookMapperImpl;
import ru.otus.hw.mapper.GenreMapperImpl;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.Provider.EXPECTED_BOOKS;
import static ru.otus.hw.Provider.FIRST_BOOK_ID;

@WebMvcTest(BookController.class)
@Import({BookMapperImpl.class, AuthorMapperImpl.class,
        GenreMapperImpl.class, SecurityConfig.class})
public class SecurityBookControllerTest {

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


    private static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor getAdmin() {
        return user("username").roles(Role.ADMIN);
    }

    private static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor getUser() {
        return user("username").roles(Role.USER);
    }

    @ParameterizedTest
    @MethodSource("argumentsBookUnauthorizedRequests")
    @DisplayName("должен все неаутифицированные запросы перенаправить на страницу логина")
    void shouldRedirectOnLoginPage(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @ParameterizedTest
    @MethodSource("argumentsBookAuthorizedRequestsAdmin")
    @DisplayName("должен все аутифицированные запросы корректно обработать с ролью админа")
    void shouldPerformAuthorizedRequestWithAdmin(MockHttpServletRequestBuilder requestBuilder, ResultMatcher expectedStatus) throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);
        when(bookService.findById(FIRST_BOOK_ID)).thenReturn(expectedBookDto);
        mockMvc.perform(requestBuilder)
                .andExpect(expectedStatus);

    }

    @ParameterizedTest
    @MethodSource("argumentsBookAuthorizedRequestsUser")
    @DisplayName("должен допустить только для разрешенного ресурса юзера")
    void shouldPerformAuthorizedRequest(MockHttpServletRequestBuilder requestBuilder, ResultMatcher expectedStatus) throws Exception {
        var expectedBookDto = expectedBookDtos.get(0);
        when(bookService.findById(FIRST_BOOK_ID)).thenReturn(expectedBookDto);
        mockMvc.perform(requestBuilder)
                .andExpect(expectedStatus);
    }

    static Stream<Arguments> argumentsBookUnauthorizedRequests() {
        var expectedBookDto = expectedBookDtos.get(0);
        return Stream.of(Arguments.of(get("/books")),
                Arguments.of(get("/books/new")),
                Arguments.of(get("/books/edit/%d".formatted(FIRST_BOOK_ID))),
                Arguments.of(post("/books")
                        .param("id", String.valueOf(expectedBookDto.getId()))
                        .param("title", expectedBookDto.getTitle())
                        .param("authorName", expectedBookDto.getAuthorName())
                        .param("genreName", expectedBookDto.getGenreName())),
                Arguments.of(post("/books/update")
                        .param("id", String.valueOf(expectedBookDto.getId()))
                        .param("title", expectedBookDto.getTitle())
                        .param("authorName", expectedBookDto.getAuthorName())
                        .param("genreName", expectedBookDto.getGenreName())),
                Arguments.of(post("/books/delete/%d".formatted(expectedBookDto.getId())))
        );
    }

    static Stream<Arguments> argumentsBookAuthorizedRequestsAdmin() {
        var expectedBookDto = expectedBookDtos.get(0);
        return Stream.of(
                Arguments.of(get("/books").with(getAdmin()),
                        status().isOk()),

                Arguments.of(get("/books/new").with(getAdmin()),
                        status().isOk()),

                Arguments.of(get("/books/edit/%d".formatted(FIRST_BOOK_ID)).with(getAdmin()),
                        status().isOk()),

                Arguments.of(post("/books")
                                .param("id", String.valueOf(expectedBookDto.getId()))
                                .param("title", expectedBookDto.getTitle())
                                .param("authorName", expectedBookDto.getAuthorName())
                                .param("genreName", expectedBookDto.getGenreName())
                                .with(getAdmin()),
                        status().is3xxRedirection()),

                Arguments.of(post("/books/update")
                                .param("id", String.valueOf(expectedBookDto.getId()))
                                .param("title", expectedBookDto.getTitle())
                                .param("authorName", expectedBookDto.getAuthorName())
                                .param("genreName", expectedBookDto.getGenreName())
                                .with(getAdmin()),
                        status().is3xxRedirection()),

                Arguments.of(post("/books/delete/%d".formatted(expectedBookDto.getId()))
                                .with(getAdmin()),
                        status().is3xxRedirection()));
    }

    static Stream<Arguments> argumentsBookAuthorizedRequestsUser() {
        var expectedBookDto = expectedBookDtos.get(0);
        return Stream.of(
                Arguments.of(get("/books").with(getUser()),
                        status().isOk()),

                Arguments.of(get("/books/new").with(getUser()),
                        status().isForbidden()),

                Arguments.of(get("/books/edit/%d".formatted(FIRST_BOOK_ID)).with(getUser()),
                        status().isForbidden(),

                        Arguments.of(post("/books")
                                        .param("id", String.valueOf(expectedBookDto.getId()))
                                        .param("title", expectedBookDto.getTitle())
                                        .param("authorName", expectedBookDto.getAuthorName())
                                        .param("genreName", expectedBookDto.getGenreName())
                                        .with(getUser()),
                                status().isForbidden()),

                        Arguments.of(post("/books/update")
                                        .param("id", String.valueOf(expectedBookDto.getId()))
                                        .param("title", expectedBookDto.getTitle())
                                        .param("authorName", expectedBookDto.getAuthorName())
                                        .param("genreName", expectedBookDto.getGenreName())
                                        .with(getUser()),
                                status().isForbidden()),

                        Arguments.of(post("/books/delete/%d".formatted(expectedBookDto.getId()))
                                        .with(getUser()),
                                status().isForbidden())));
    }

}
