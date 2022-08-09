package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommonStorage<E extends AbstractEntity> implements CommonStorage<E> {
    protected final JdbcTemplate jdbcTemplate;
    protected final Map<Long, E> entities = new HashMap<>();
    private long id = 1L;

    public AbstractCommonStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public E create(E entity) {
        long newId = id++;
        while(entities.containsKey(newId)) {
            newId = id++; // Подбираем незанятый id
        }
        entity.setId(newId);
        entities.put(newId, entity);
        return entity;
    }

    @Override
    public E update(E entity) {
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E delete(Long id) {
        return entities.remove(id);
    }

    @Override
    public void deleteAll() {
        entities.clear();
        id = 1L;
    }

    @Override
    public E getById(long id) {
        return entities.get(id);
    }

    @Override
    public Collection<E> getAll() {
        return entities.values();
    }
}
