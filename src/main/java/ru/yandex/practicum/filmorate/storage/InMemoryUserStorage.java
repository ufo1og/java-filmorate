package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage extends AbstractCommonStorage<User> implements UserStorage {

    public void addFriend(long userId, long friendId) {
        entities.get(userId).addFriend(friendId);
    }

    public void removeFriend(long userId, long friendId) {
        entities.get(userId).removeFriend(friendId);
    }
}
