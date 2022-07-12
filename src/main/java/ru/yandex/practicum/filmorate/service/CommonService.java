package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.AbstractEntity;

import java.util.Collection;

public interface CommonService<E extends AbstractEntity> {
    E create(E entity);

    E update(E entity);

    E delete(long id);

    void deleteAll();

    E getById(long id);

    Collection<E> getAll();
}
