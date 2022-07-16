package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FilmControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FilmStorage storage;

    @AfterEach
    private void clearStorage() {
        storage.deleteAll();
    }

    @Test
    public void whenCreatedFilm_thenStatus200() {
        Film film = new Film("film", "description", LocalDate.of(2000, 1, 1), 120);

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        film.setId(1);
        Assertions.assertEquals(film, response.getBody());
    }

    @Test
    public void whenCreatingNotValid_thenStatus400() {
        // negative duration
        Film film = new Film("film", "description", LocalDate.of(2000, 1, 1), -120);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // blank name
        film = new Film("", "description", LocalDate.of(2000, 1, 1), 120);
        response = restTemplate.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // null name
        film = new Film(null, "description", LocalDate.of(2000, 1, 1), 120);
        response = restTemplate.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // bad releaseDate
        film = new Film("film", "description", LocalDate.of(1800, 1, 1), 120);
        response = restTemplate.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenUpdatedFilm_thenStatus200() {
        Film film = new Film("film", "description", LocalDate.of(2000, 1, 1), 120);
        storage.create(film);

        film = new Film("new_film", "new_description", LocalDate.of(2022, 1, 1), 100);
        film.setId(1);
        HttpEntity<Film> entity = new HttpEntity<>(film);

        ResponseEntity<Film> response = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(film, response.getBody());
    }

    @Test
    public void whenUpdatingFilmWithNegativeId_thenStatus404() {
        Film film = new Film("film", "description", LocalDate.of(2000, 1, 1), 120);
        film.setId(-1);
        HttpEntity<Film> entity = new HttpEntity<>(film);
        ResponseEntity<Film> response = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenUpdatingFilmThatNotExist_thenStatus404() {
        Film film = new Film("film", "description", LocalDate.of(2000, 1, 1), 120);
        film.setId(777);
        HttpEntity<Film> entity = new HttpEntity<>(film);
        ResponseEntity<Film> response = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenGetFilms_thenStatus200() {
        Film film = new Film("film", "description", LocalDate.of(2000, 1, 1), 120);
        storage.create(film);
        ResponseEntity<List<Film>> response = restTemplate.exchange("/films", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        film.setId(1);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(List.of(film), response.getBody());
    }

    @Test
    public void whenGetFilm_thenStatus200() {
        Film film = new Film("film", "description", LocalDate.of(2000, 1, 1), 120);
        storage.create(film);
        film.setId(1);
        ResponseEntity<Film> response = restTemplate.exchange("/films/{id}", HttpMethod.GET, null, Film.class, 1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(film, response.getBody());
    }

    @Test
    public void whenGetFilmThatNotExist_thenStatus404() {
        ResponseEntity<Film> response = restTemplate.exchange("/films/{id}", HttpMethod.GET, null, Film.class, 777);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
