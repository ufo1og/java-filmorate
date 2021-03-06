package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractCommonStorage<Film> implements FilmStorage {
    @Override
    public void addLike(long filmId, long userId) {
        entities.get(filmId).addLike(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        entities.get(filmId).removeLike(userId);
    }
}
