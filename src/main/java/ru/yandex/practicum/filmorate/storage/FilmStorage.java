package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage extends CommonStorage<Film> {

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}
