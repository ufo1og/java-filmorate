package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testCreateGetUpdateAndDeleteFilm() {
        Film film = new Film("film", "desc",
                LocalDate.of(2000, 1, 1), 120, new Mpa(1, null));
        // Проверка создания фильма
        Film createdFilm = filmDbStorage.create(film);
        Assertions.assertEquals(film, createdFilm);
        // Проверка получения фильма
        Film receivedFilm = filmDbStorage.getById(1L);
        Assertions.assertEquals(film, receivedFilm);
        // Проверка обновления фильма
        Film newFilm = new Film("newFilm", "newDesc",
                LocalDate.of(2001, 1, 1), 121, new Mpa(2, null));
        newFilm.setId(1L);
        Film updatedFilm = filmDbStorage.update(newFilm);
        Assertions.assertEquals(newFilm, updatedFilm);
        // Проверка удаления фальма
        filmDbStorage.delete(1L);
        EntityNotFoundException e = Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> filmDbStorage.getById(1L)
        );
        Assertions.assertEquals("Film with id = '1' not found.", e.getMessage());
    }

}
