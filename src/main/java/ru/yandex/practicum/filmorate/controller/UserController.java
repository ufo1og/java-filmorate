package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User postUser(@RequestBody @Valid User user) {
        if ("".equals(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        log.debug("Добавлен пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody @Valid User user) {
        if (user.getId() < 0) {
            log.debug("Ошибка валидации: Переданный id меньше 0");
            throw new ValidationException("User id can't be less than 0");
        }
        if ("".equals(user.getName())) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(id++); // Если id не передан в теле запроса - присваиваем свой id
            log.debug("Добавлен пользователь: {}", user);
        }
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь {} изменен на {}", users.get(user.getId()), user);
        }
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
