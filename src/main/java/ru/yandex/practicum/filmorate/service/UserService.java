package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
        setNameIfNotExist(entity);
        return super.update(entity);
    }

    private void setNameIfNotExist(User entity) {
        if ("".equals(entity.getName()) || entity.getName() == null) {
            entity.setName(entity.getLogin());
        }
    }
}
