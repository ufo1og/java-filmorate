package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractCommonStorage<Film> implements FilmStorage {
}
