package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Service
public class FilmService extends AbstractCommonService<Film, FilmStorage> {
    public FilmService(@Qualifier("InMemoryFilmStorage") FilmStorage storage) {
        super(storage);
    }
}
