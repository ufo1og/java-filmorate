package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.utility.ValidFilm;

import java.time.LocalDate;

@Data
@ValidFilm
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration; // в минутах
}
