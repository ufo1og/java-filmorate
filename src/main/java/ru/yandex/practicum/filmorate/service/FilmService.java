package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends AbstractCommonService<Film, FilmStorage> {
    private final UserStorage userStorage;

    public FilmService(
            @Qualifier("InMemoryFilmStorage") FilmStorage storage,
            @Qualifier("UserDbStorage") UserStorage userStorage
            ) {
        super(storage);
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        log.debug("Added like from User with id = {} to Film with id = {}", userId, filmId);
        storage.getById(filmId).addLike(userId);
    }

    public void removeLike(long filmId, long userId) {
        log.debug("Removed like from User with id = {} to Film with id = {}", userId, filmId);
        storage.getById(filmId).removeLike(userId);
    }

    public List<Film> getMostPopularFilms(int count) {
        return storage.getAll().stream()
                .sorted((o1, o2) -> o2.getUserWhoLikesIds().size() - o1.getUserWhoLikesIds().size())
                .limit(count).collect(Collectors.toList());
    }
}
