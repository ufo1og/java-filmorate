package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService extends AbstractCommonService<Film, FilmStorage> {

    private final LikesStorage likesStorage;

    public FilmService(
            @Qualifier("FilmDbStorage") FilmStorage storage,
            LikesStorage likesStorage
            ) {
        super(storage);
        this.likesStorage = likesStorage;
    }

    public void addLike(long filmId, long userId) {
        log.debug("Added like from User with id = {} to Film with id = {}", userId, filmId);
        likesStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.debug("Removed like from User with id = {} to Film with id = {}", userId, filmId);
        likesStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(int count) {
        return likesStorage.getMostPopularFilms(count);
    }

    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    public Genre getGenre(int id) {
        return storage.getGenre(id);
    }

    public List<Mpa> getAllMpas() {
        return storage.getAllMpas();
    }

    public Mpa getMpa(int id) {
        return storage.getMpa(id);
    }
}
