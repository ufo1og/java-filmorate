package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.utility.ValidFilm;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ValidFilm
public class Film extends AbstractEntity{
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
}
