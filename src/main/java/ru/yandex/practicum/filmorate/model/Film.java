package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.utility.ValidFilm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
    private final Collection<Genre> genres = new ArrayList<>();

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}