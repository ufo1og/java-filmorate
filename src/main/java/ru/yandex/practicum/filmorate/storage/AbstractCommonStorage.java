package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AbstractEntity;

import java.util.Collection;
import java.util.Map;

public class AbstractCommonStorage<E extends AbstractEntity> implements CommonStorage<E> {
    private Map<Long, E> entities;
    private long id = 1L;

    @Override
    public E create(E entity) {
        long newId = id++;
        while(entities.containsKey(newId)) {
            newId = id++; // Подбираем незанятый id
        }
        entity.setId(newId);
        return entities.put(newId, entity);
    }

    @Override
    public E update(E entity) {
        return entities.put(entity.getId(), entity);
    }

    @Override
    public E delete(Long id) {
        return entities.remove(id);
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }

    @Override
    public E getById(Long id) {
        return entities.get(id);
    }

    @Override
    public Collection<E> getAll() {
        return entities.values();
    }
}
