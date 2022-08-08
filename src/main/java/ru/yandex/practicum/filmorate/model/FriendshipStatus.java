package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class FriendshipStatus {
    private int id;
    private int name;
}
