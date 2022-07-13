package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractCommonController<Film, FilmService> {

    public FilmController(FilmService filmService) {
        super(filmService);
    }
}
