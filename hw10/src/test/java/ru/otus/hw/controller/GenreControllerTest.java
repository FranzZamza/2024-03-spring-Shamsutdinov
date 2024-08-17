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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.mapper.GenreMapperImpl;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import(GenreMapperImpl.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    ObjectMapper objectMapper;

    private static List<GenreDto> expectedGenreDtos;

    @BeforeAll
    static void beforeAll(@Autowired GenreMapper genreMapper) {
        var firstGenre = new Genre(1, "first genre");
        var secondGenre = new Genre(2, "second genre");
        var thirdGenre = new Genre(3, "third genre");

        var genres = List.of(firstGenre, secondGenre, thirdGenre);

        expectedGenreDtos = genreMapper.toDto(genres);
    }

    @Test
    @DisplayName("должен вернуть все жанры")
    void shouldReturnAllGenres() throws Exception {
        when(genreService.findAll()).thenReturn(expectedGenreDtos);

        mockMvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenreDtos)));

        verify(genreService, times(1)).findAll();
    }
}