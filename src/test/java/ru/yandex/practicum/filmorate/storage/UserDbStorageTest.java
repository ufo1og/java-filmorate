package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    public void testCreateGetUpdateAndDeleteUser() {
        User user = new User("user@mail.ru", "login", LocalDate.of(2000, 1, 1));
        // Проверка создания пользователя
        User createdUser = userDbStorage.create(user);
        user.setId(1L);
        user.setName(user.getLogin());
        Assertions.assertEquals(user, createdUser);
        // Проверка получения пользователя
        User receivedUser = userDbStorage.getById(1L);
        Assertions.assertEquals(user, receivedUser);
        // Проверка обновления пользователя
        User newUser = new User("newUser@mail", "newLogin", LocalDate.of(2001, 1, 1));
        newUser.setId(1L);
        newUser.setName("newName");
        User updatedUser = userDbStorage.update(newUser);
        Assertions.assertEquals(newUser, updatedUser);
        // Проверка удаления пользователя
        userDbStorage.delete(1L);
        EntityNotFoundException e = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userDbStorage.getById(1L)
        );
        Assertions.assertEquals("User with id = '1' not found.", e.getMessage());
    }
}
