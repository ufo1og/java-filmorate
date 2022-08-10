package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.utility.ValidFilm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
@ValidFilm
public class Film extends AbstractEntity {
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
    private final Mpa mpa;
    private final Set<Genre> genres = new HashSet<>();

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}