package ru.otus.hw.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.AuthorMapperImpl;
import ru.otus.hw.model.Author;
import ru.otus.hw.service.AuthorService;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(AuthorController.class)
@Import({AuthorMapperImpl.class})
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    ObjectMapper objectMapper;

    private static List<AuthorDto> expectedAuthorDtos;

    @BeforeAll
    static void beforeAll(@Autowired AuthorMapper authorMapper) {
        var firstAuthor = new Author(1, "test name 1");
        var secondAuthor = new Author(2, "test name 2");
        var thierdAuthor = new Author(3, "test name 3");

        var authors = List.of(firstAuthor, secondAuthor, thierdAuthor);

        expectedAuthorDtos = authorMapper.toDto(authors);
    }

    @Test
    @DisplayName("должен вернуть всех авторов")
    void shouldReturnAuthors() throws Exception {
        when(authorService.findAll()).thenReturn(expectedAuthorDtos);

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthorDtos)));

        verify(authorService, times(1)).findAll();
    }

}