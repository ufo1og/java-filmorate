package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;


import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Repository("UserDbStorage")
public class UserDbStorage extends AbstractCommonStorage<User> implements UserStorage {
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendsStorage friendsStorage) {
        super(jdbcTemplate);
        this.friendsStorage = friendsStorage;
    }

    @Override
    public User create(User entity) {
        String sqlQuery = "INSERT INTO users(email, login, user_name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getLogin());
            final String name = entity.getName();
            if (name.isBlank() || name == null) {
                entity.setName(entity.getLogin()); // Если имя не задано то в поле имя заносится логин
            }
            stmt.setString(3, entity.getName());
            final LocalDate birthday = entity.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(entity.getBirthday()));
            }
            return stmt;
        }, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
        return entity;
    }

    @Override
    public User update(User entity) {
        String sqlQuery = "UPDATE users set" +
                " email = ?," +
                " login = ?," +
                " user_name = ?," +
                " birthday = ?" +
                " WHERE user_id = ?";

        if (jdbcTemplate.update(sqlQuery, entity.getEmail(), entity.getLogin(), entity.getName(), entity.getBirthday(),
                entity.getId()) == 0) {
            throw new EntityNotFoundException("User not found.");
        }

        return entity;
    }

    @Override
    public User delete(Long id) {
        User deletingUser = getById(id);
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
        return deletingUser;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("TRUNCATE TABLE users");
    }

    @Override
    public User getById(long id) {
        String sqlQuery = "SELECT user_id, email, login, user_name, birthday" +
                " FROM users" +
                " WHERE user_id = ?";

        List<User> queryResult = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> buildUserFromResultSet(rs), id);
        if (queryResult.size() == 0) {
            throw new EntityNotFoundException("User with id = '" + id + "' not found.");
        }
        User user = queryResult.get(0);
        for (long i : friendsStorage.getUserFriends(id)) {
            user.addFriend(i);
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT user_id, email, login, user_name, birthday" +
                " FROM users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> buildUserFromResultSet(rs));
    }

    private User buildUserFromResultSet(ResultSet rs) throws SQLException {
        return User.builder().id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
