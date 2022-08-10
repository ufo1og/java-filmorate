package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommonStorage<E extends AbstractEntity> implements CommonStorage<E> {
    protected final JdbcTemplate jdbcTemplate;

    public AbstractCommonStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
