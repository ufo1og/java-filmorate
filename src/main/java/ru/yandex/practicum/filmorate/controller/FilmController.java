package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film postFilm(@RequestBody @Valid Film film) {
        film.setId(id++);
        log.debug("Добавлен фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody @Valid Film film) {
        if (film.getId() < 0) {
            log.debug("Ошибка валидации: Переданный id меньше 0");
            throw new ValidationException("Film id can't be less than 0");
        }
        if (films.containsKey(film.getId())) {
            log.debug("Фильм {} изменен на {}", films.get(film.getId()), film);
        }
        if (film.getId() == 0) {
            film.setId(id++); // Если id не передан в теле запроса - присваиваем свой id
            log.debug("Добавлен фильм: {}", film);
        }
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
