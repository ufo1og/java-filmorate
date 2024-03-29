package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.utility.ValidUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@ValidUser
public class User extends AbstractEntity {
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(long id) {
        friends.add(id);
    }
}
