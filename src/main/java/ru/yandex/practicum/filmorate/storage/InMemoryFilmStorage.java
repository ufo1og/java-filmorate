package ru.yandex.practicum.filmorate.storage;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(id++);
        return films.put(film.getId(), film);
    }

    @Override
    public Film updateFilm(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public Film removeFilm(int filmId) {
        return films.remove(filmId);
    }

    @Override
    public Film getFilm(int filmId) {
        return films.get(filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
