package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final Film film = new Film("film", "desc",
            LocalDate.of(2000, 1, 1), 120, new Mpa(1, null));

    @Test
    @Order(1)
    public void testCreatingFilm() {
        Film createdFilm = filmDbStorage.create(film);
        Assertions.assertEquals(film, createdFilm);
    }

    @Test
    @Order(2)
    public void testGettingFilm() {
        film.setId(1L);
        Film receivedFilm = filmDbStorage.getById(1L);
        Assertions.assertEquals(film, receivedFilm);
    }

    @Test
    @Order(3)
    public void testUpdatingFilm() {
        Film newFilm = new Film("newFilm", "newDesc",
                LocalDate.of(2001, 1, 1), 121, new Mpa(2, null));
        newFilm.setId(1L);
        Film updatedFilm = filmDbStorage.update(newFilm);
        Assertions.assertEquals(newFilm, updatedFilm);
    }

    @Test
    @Order(4)
    public void testDeletingFilm() {
        filmDbStorage.delete(1L);
        EntityNotFoundException e = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> filmDbStorage.getById(1L)
        );
        Assertions.assertEquals("Film with id = '1' not found.", e.getMessage());
    }

}
