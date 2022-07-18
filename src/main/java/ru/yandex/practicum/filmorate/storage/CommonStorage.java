package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AbstractEntity;

import java.util.Collection;

public interface CommonStorage<E extends AbstractEntity> {
    E create(E entity);

    E update(E entity);

    E delete(Long id);

    void deleteAll();

    E getById(long id);

    Collection<E> getAll();
}
