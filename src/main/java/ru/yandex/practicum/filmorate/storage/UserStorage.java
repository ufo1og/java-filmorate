package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage extends CommonStorage<User> {

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);
}
