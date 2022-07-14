package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractCommonService<Film, FilmStorage> {
    private final UserStorage userStorage;

    public FilmService(
            @Qualifier("InMemoryFilmStorage") FilmStorage storage,
            @Qualifier("InMemoryUserStorage") UserStorage userStorage
            ) {
        super(storage);
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        throwExceptionIfEntityNotExist(filmId);
        throwExceptionIfUserNotExist(userId);
        storage.getById(filmId).addLike(userId);
    }

    public void removeLike(long filmId, long userId) {
        throwExceptionIfEntityNotExist(filmId);
        throwExceptionIfUserNotExist(userId);
        storage.getById(filmId).removeLike(userId);
    }

    public List<Film> getMustPopularFilms(long count) {
        return storage.getAll().stream()
                .sorted(Comparator.comparingInt(p -> p.getUserWhoLikesIds().size()))
                .limit(count).collect(Collectors.toList());
    }

    private void throwExceptionIfUserNotExist(long id) {
        if (userStorage.getById(id) == null) {
            throw new EntityNotFoundException("User with id = " + id + "not found.");
        }
    }
}
