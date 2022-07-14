package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage extends AbstractCommonStorage<User> implements UserStorage {

    @Override
    public void addFriend(long userId, long friendId) {
        entities.get(userId).addFriend(friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        entities.get(userId).removeFriend(friendId);
    }
}
