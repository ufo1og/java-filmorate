package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage extends CommonStorage<Film> {
    List<Genre> getAllGenres();

    Genre getGenre(int id);

    List<Mpa> getAllMpas();

    Mpa getMpa(int id);
}
