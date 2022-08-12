package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode
public abstract class AbstractEntity {
    private long id;

    public AbstractEntity() {
    }
}
