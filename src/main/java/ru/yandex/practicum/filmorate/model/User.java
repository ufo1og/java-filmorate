package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.utility.ValidUser;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ValidUser
public class User extends AbstractEntity {
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
}
