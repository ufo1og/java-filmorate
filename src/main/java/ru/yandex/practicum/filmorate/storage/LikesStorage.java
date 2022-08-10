package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getMostPopularFilms(int count);
}
