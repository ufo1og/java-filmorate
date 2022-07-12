package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(id++);
        return users.put(user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public User removeUser(int userId) {
        return users.remove(userId);
    }

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
