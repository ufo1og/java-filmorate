package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractCommonService<User, UserStorage> {
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage storage, FriendsStorage friendsStorage) {
        super(storage);
        this.friendsStorage = friendsStorage;
    }

    @Override
    public User create(User entity) {
        setNameIfNotExist(entity);
        return super.create(entity);
    }

    @Override
    public User update(User entity) {
        setNameIfNotExist(entity);
        return super.update(entity);
    }

    public void addFriend(long userId, long friendId) {
        log.debug("Users with id = {} and id = {} are now friends", userId, friendId);
        friendsStorage.addFriend(userId, friendId, 0);
    }

    public void removeFriend(long userId, long friendId) {
        log.debug("Users with id = {} and id = {} are not friends anymore", userId, friendId);
        friendsStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        return friendsStorage.getUserFriends(userId).stream()
                .map(storage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        return friendsStorage.getUserCommonFriends(userId, otherUserId).stream()
                .map(storage::getById).collect(Collectors.toList());
    }

    private void setNameIfNotExist(User entity) {
        if ("".equals(entity.getName()) || entity.getName() == null) {
            entity.setName(entity.getLogin());
        }
    }
}
