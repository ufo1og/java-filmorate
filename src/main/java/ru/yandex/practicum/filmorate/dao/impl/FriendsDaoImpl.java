package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.util.List;

@Repository
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Long> getUserFriends(long userId) {
        String sqlQuery = "SELECT user_from AS friend" +
                " FROM friends" +
                " WHERE user_to = ?1 AND status_id = 1" +
                " UNION" +
                " SELECT user_to AS friend" +
                " FROM friends" +
                " WHERE user_from = ?1"; // AND status_id = 1

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("friend"), userId);
    }

    @Override
    public List<Long> getUserCommonFriends(long user1Id, long user2Id) {
        String sqlQuery = "WITH user_friends (friend)" +
                " AS (" +
                " SELECT user_from as friend" +
                " FROM friends" +
                " WHERE user_to IN (?1, ?2) AND status_id = 1 " +
                " UNION ALL" +
                " SELECT user_to as friend" +
                " FROM friends" +
                " WHERE user_from IN (?1, ?2))" + //  AND status_id = 1
                " SELECT friend" +
                " FROM user_friends" +
                " GROUP BY friend" +
                " HAVING COUNT(*) > 1";


        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("friend"), user1Id, user2Id);
    }

    @Override
    public void addFriend(long userId, long friendId, int status) {
        String sqlQuery = "INSERT INTO friends (user_from, user_to, status_id)" +
                " VALUES (?, ?, ?)";

        try {
            jdbcTemplate.update(sqlQuery, userId, friendId, status);
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("User not found.");
        }
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_from = ? AND user_to = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
