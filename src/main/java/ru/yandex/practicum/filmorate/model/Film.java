package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.utility.ValidFilm;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@ValidFilm
public class Film extends AbstractEntity {
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
    private final Rating rating;
    private final Collection<Genre> genres;
    private final Set<Long> userWhoLikesIds = new HashSet<>();

    public void addLike(long id) {
        userWhoLikesIds.add(id);
    }

    public void removeLike(long id) {
        userWhoLikesIds.remove(id);
    }
}