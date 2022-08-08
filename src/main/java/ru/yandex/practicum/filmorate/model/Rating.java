package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Rating {
    private int id;
    private String name;
}
