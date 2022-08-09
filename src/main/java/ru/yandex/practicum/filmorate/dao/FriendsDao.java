package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface FriendsDao {
    List<Long> getUserFriends(long userId);

    List<Long> getUserCommonFriends(long user1Id, long user2Id);

    void addFriend(long userId, long friendId, int status);

    void removeFriend(long userId, long friendId);
}
