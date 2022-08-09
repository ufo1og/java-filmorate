package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
public abstract class AbstractEntity {
    private long id;

    public AbstractEntity() {
    }
}
