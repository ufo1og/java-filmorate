package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.util.Collections;
import java.util.List;

@Repository("LikesDbStorage")
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException(
                    String.format("Film with id = '%s' or User with id = '%s' not found.", filmId, userId));
        }
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
        try {
            if (jdbcTemplate.update(sqlQuery, filmId, userId) == 0) {
                throw new EntityNotFoundException(
                        String.format("Film with id = '%s' or User with id = '%s' not found.", filmId, userId));
            }
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException(
                    String.format("Film with id = '%s' or User with id = '%s' not found.", filmId, userId));
        }
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sqlQuery = "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, f.mpa_id" +
                " FROM films as f" +
                " LEFT JOIN films_likes as fl ON f.film_id = fl.film_id" +
                " GROUP BY f.film_id" +
                " ORDER BY COUNT(*) DESC, f.film_id DESC" +
                " LIMIT ?";

        List<Film> popularFilms = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> FilmDbStorage.buildFilmFromResultSet(rs), count);

        if (popularFilms == null) {
            return Collections.emptyList();
        }

        return popularFilms;
    }
}
