package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.storage.CommonStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommonService <E extends AbstractEntity, S extends CommonStorage<E>>
        implements CommonService<E> {

    protected final S storage;

    @Override
    public E create(E entity) {
        E createdEntity = storage.create(entity);
        log.debug("Created entity {} in storage: {}", createdEntity.getClass().getSimpleName(), createdEntity);
        return createdEntity;
    }

    @Override
    public E update(E entity) {
        E updatedEntity = storage.update(entity);
        log.debug("Updated entity {} in storage: {}", updatedEntity.getClass().getSimpleName(), updatedEntity);
        return updatedEntity;
    }

    @Override
    public E delete(long id) {
        E deletedEntity = storage.delete(id);
        log.debug("Deleted entity {} from storage: {}", deletedEntity.getClass().getSimpleName(), deletedEntity);
        return deletedEntity;
    }

    @Override
    public void deleteAll() {
        log.debug("Storage {} cleared", storage.getClass().getSimpleName());
        storage.deleteAll();
    }

    @Override
    public E getById(long id) {
        return storage.getById(id);
    }

    @Override
    public Collection<E> getAll() {
        return storage.getAll();
    }
}
