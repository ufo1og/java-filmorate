package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository("FilmDbStorage")
public class FilmDbStorage extends AbstractCommonStorage<Film> implements FilmStorage {

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Film create(Film entity) {
        String sqlQuery = "INSERT INTO films(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)" +
                " VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setDate(3, Date.valueOf(entity.getReleaseDate()));
            stmt.setLong(4, entity.getDuration());
            stmt.setInt(5, entity.getMpa().getId());
            return stmt;
        }, keyHolder);

        entity.setId(keyHolder.getKey().longValue());
        loadFilmsMpa(Collections.singletonList(entity));
        if (entity.getGenres() != null) {
            setFilmsGenres(Collections.singletonList(entity));
        }
        return entity;
    }

    @Override
    public Film update(Film entity) {
        String sqlQuery = "UPDATE films set " +
                " film_name = ?," +
                " description = ?," +
                " release_date = ?," +
                " duration = ?," +
                " mpa_id = ?" +
                " WHERE film_id = ?";

        if (jdbcTemplate.update(sqlQuery,
                entity.getName(),
                entity.getDescription(),
                entity.getReleaseDate(),
                entity.getDuration(),
                entity.getMpa().getId(),
                entity.getId()) == 0) {
            throw new EntityNotFoundException(String.format("Film with id = '%s' not found.", entity.getId()));
        }

        setFilmsGenres(Collections.singletonList(entity));
        return entity;
    }

    @Override
    public Film delete(Long id) {
        Film deletingFilm = getById(id);
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
        return deletingFilm;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("TRUNCATE TABLE films");
    }

    @Override
    public Film getById(long id) {
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, mpa_id" +
                " FROM films WHERE film_id = ?";

        List<Film> queryResult = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> buildFilmFromResultSet(rs), id);
        if (queryResult.size() == 0) {
            throw new EntityNotFoundException(String.format("Film with id = '%s' not found.", id));
        }
        Film film = queryResult.get(0);
        loadFilmsMpa(Collections.singletonList(film));
        loadFilmsGenres(Collections.singletonList(film));
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "SELECT film_id, film_name, description, release_date, duration, mpa_id FROM films";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> buildFilmFromResultSet(rs));
        if (films.size() == 0) {
            return Collections.emptyList();
        }
        loadFilmsMpa(films);
        loadFilmsGenres(films);
        return films;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres ORDER BY genre_id";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNun) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }

    @Override
    public Genre getGenre(int id) {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";

        List<Genre> genres = jdbcTemplate.query(sqlQuery,
                (rs, rowNun) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name")), id);
        if (genres.size() == 0) {
            throw new EntityNotFoundException(String.format("Genre with id = '%s' not found.", id));
        }
        return genres.get(0);
    }

    @Override
    public List<Mpa> getAllMpas() {
        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa ORDER BY mpa_id";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
    }

    @Override
    public Mpa getMpa(int id) {
        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";

        List<Mpa> mpas = jdbcTemplate.query(sqlQuery,
                (rs, rowNun) -> new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")), id);
        if (mpas.size() == 0) {
            throw new EntityNotFoundException(String.format("Mpa with id = '%s' not found.", id));
        }
        return mpas.get(0);
    }

    private void loadFilmsMpa(List<Film> films) {
        String sqlQuery = "SELECT m.mpa_name FROM mpa as m" +
                " JOIN films f on m.mpa_id = f.mpa_id AND f.film_id = ?";

        films.stream().forEach(f -> f.getMpa().setName(
                jdbcTemplate.query(sqlQuery,
                        (rs, rowNum) -> rs.getString("mpa_name"), f.getId()).get(0)));
    }

    private void loadFilmsGenres(List<Film> films) {
        List<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
        String params = ids.stream().map(Object::toString).collect(Collectors.joining(", "));
        String sqlQuery = String.format("SELECT fg.film_id, g.genre_id, g.genre_name FROM genres AS g" +
                " JOIN films_genres AS fg ON g.genre_id = fg.genre_id AND fg.film_id IN (%s)", params);

        Map<Long, List<Genre>> filmsGenres = new HashMap<>();
        for (Film film : films) {
            filmsGenres.put(film.getId(), new ArrayList<>());
        }

        jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> filmsGenres.get(rs.getLong("film_id"))
                        .add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name"))));

        for (Film film : films) {
            for (Genre genre : filmsGenres.get(film.getId())) {
                film.addGenre(genre);
            }
        }
    }

    private void setFilmsGenres(List<Film> films) {
        List<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
        String params = ids.stream().map(Object::toString).collect(Collectors.joining(", "));
        final String sqlQueryForDelete = String.format("DELETE FROM films_genres WHERE film_id IN (%s)", params);
        jdbcTemplate.update(sqlQueryForDelete); // Удаляем имеющиеся записи о жанрах
        List<String> values = new ArrayList<>();
        for (Film film : films) {
            if (!film.getGenres().isEmpty()) {
                values.add(film.getGenres().stream()
                        .map(g -> String.format("(%s, %s)", film.getId(), g.getId()))
                        .distinct().collect(Collectors.joining(", ")));
            }
        }
        if (!values.isEmpty()) {
            String sqlQueryHead = "INSERT INTO films_genres (film_id, genre_id) VALUES";
            final String sqlQueryForInsert = sqlQueryHead + String.join(", ", values);
            jdbcTemplate.update(sqlQueryForInsert);
        }
    }

    static Film buildFilmFromResultSet(ResultSet rs) throws SQLException {
        return Film.builder().id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpa(new Mpa(rs.getInt("mpa_id"), null))
                .build();
    }
}
