package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FilmController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postFilm_Default() throws Exception {
        Film film = new Film("film", "description", LocalDate.now(), 150);
        MvcResult result = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andReturn();

        Film responseFilm = objectMapper.readValue(result.getResponse().getContentAsString(), Film.class);
        film.setId(responseFilm.getId());
        Assertions.assertEquals(film, responseFilm);
    }

    @Test
    public void postFilm_EmptyRequestBody() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postFilm_NegativeId() {
        Film film = new Film("film", "description", LocalDate.now(), 150);
        film.setId(-1L);

        NestedServletException e = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(post("/films")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(film)))
                        .andExpect(status().is5xxServerError())
        );
        String expectedMessage = "Request processing failed; nested exception is javax.validation." +
                "ValidationException: HV000028: Unexpected exception during isValid call.";
        Assertions.assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    public void postFilm_NullName() throws Exception {
        Film film = new Film(null, "description", LocalDate.now(), 150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void postFilm_BlankName() throws Exception {
        Film film = new Film("", "description", LocalDate.now(), 150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void postFilm_NullDescription() throws Exception {
        Film film = new Film("film", null, LocalDate.now(), 150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void postFilm_BlankDescription() throws Exception {
        Film film = new Film("film", "", LocalDate.now(), 150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void postFilm_TooLongDescription() throws Exception {
        String desc = String.format("start%300s", "end");
        Film film = new Film("film", desc, LocalDate.now(), 150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void postFilm_NullReleaseDate() throws Exception {
        Film film = new Film("film", "description", null, 150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void postFilm_VeryOldReleaseDate() throws Exception {
        Film film = new Film("film", "description", LocalDate.of(1000, 1, 1), 150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void postFilm_NegativeDuration() throws Exception {
        Film film = new Film("film", "description", LocalDate.now(), -150);

        mvcPerformPostIsBadRequest(film);
    }

    @Test
    public void putFilm_JsonWithoutFilmId() throws Exception {
        Film film = new Film("film", "description", LocalDate.now(), 150);

        MvcResult result = getMvcResultWithPutMethod(film);

        Film responseFilm = objectMapper.readValue(result.getResponse().getContentAsString(), Film.class);
        film.setId(responseFilm.getId());
        Assertions.assertEquals(film, responseFilm);
    }

    @Test
    public void putFilm_JsonWithUnoccupiedId() throws Exception {
        Film film = new Film("film", "description", LocalDate.now(), 150);
        film.setId(1234567890L);

        MvcResult result = getMvcResultWithPutMethod(film);

        Film responseFilm = objectMapper.readValue(result.getResponse().getContentAsString(), Film.class);
        Assertions.assertEquals(film, responseFilm);
    }

    @Test
    public void putFilm_JsonWithOccupiedId() throws Exception {
        Film film = new Film("film", "description", LocalDate.now(), 150);
        film.setId(12345L);
        // Создали новый фильм с id 12345 и проверили что он создался
        MvcResult result = getMvcResultWithPutMethod(film);
        Film responseFilm = objectMapper.readValue(result.getResponse().getContentAsString(), Film.class);
        Assertions.assertEquals(film, responseFilm);

        film = new Film("new film", "new description", LocalDate.now(), 240);
        film.setId(12345L);
        // Перезаписываем фильм с id 12345 и проверяем
        result = getMvcResultWithPutMethod(film);
        responseFilm = objectMapper.readValue(result.getResponse().getContentAsString(), Film.class);
        Assertions.assertEquals(film, responseFilm);
    }

    @Test
    @Order(1)
    // Важно чтобы тест выполнялся первым, пока не добавлен ни один фильм
    public void getFilms_NoFilms() throws Exception {
        MvcResult result = mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn();

        List<Film> responseFilms = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});
        Assertions.assertEquals(Collections.emptyList(), responseFilms);
    }

    @Test
    @Order(2)
    public void getFilms_Default() throws Exception {
        List<Film> films = new ArrayList<>();
        // Генерируем фильмы и добавляем их через PUT запросы
        for (int i = 0; i < 5; i++) {
            Film film = new Film("Film" + i, "Desc" + i, LocalDate.now(), 110);
            film.setId(5000L + i);
            films.add(film);
            getMvcResultWithPutMethod(film);
        }

        MvcResult result = mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn();

        List<Film> responseFilms = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});
        Assertions.assertEquals(films, responseFilms);
    }

    private MvcResult getMvcResultWithPutMethod(Film film) throws Exception {
        return mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void mvcPerformPostIsBadRequest(Film film) throws Exception {
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}
