package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractCommonService<User, UserStorage> {
    public UserService(@Qualifier("InMemoryUserStorage") UserStorage storage) {
        super(storage);
    }

    @Override
    public User create(User entity) {
        setNameIfNotExist(entity);
        return super.create(entity);
    }

    @Override
    public User update(User entity) {
        throwExceptionIfEntityNotExist(entity.getId());
        setNameIfNotExist(entity);
        return super.update(entity);
    }

    public void addFriend(long userId, long friendId) {
        throwExceptionIfEntityNotExist(userId);
        throwExceptionIfEntityNotExist(friendId);
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        log.debug("Users with id = {} and id = {} are now friends", userId, friendId);
        storage.addFriend(userId, friendId);
        storage.addFriend(friendId, userId);
    }

    public void removeFriend(long userId, long friendId) {
        throwExceptionIfEntityNotExist(userId);
        throwExceptionIfEntityNotExist(friendId);
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        log.debug("Users with id = {} and id = {} are not friends anymore", userId, friendId);
        storage.removeFriend(userId, friendId);
        storage.removeFriend(friendId, userId);
    }

    public List<User> getFriends(long userId) {
        throwExceptionIfEntityNotExist(userId);
        return storage.getById(userId).getFriends().stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        throwExceptionIfEntityNotExist(userId);
        throwExceptionIfEntityNotExist(otherUserId);
        return storage.getById(userId).getFriends().stream()
                .filter(storage.getById(otherUserId).getFriends()::contains)
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    private void setNameIfNotExist(User entity) {
        if ("".equals(entity.getName()) || entity.getName() == null) {
            entity.setName(entity.getLogin());
        }
    }
}
