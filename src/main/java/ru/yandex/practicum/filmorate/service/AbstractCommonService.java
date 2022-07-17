package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.storage.CommonStorage;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.util.Collection;

@Slf4j
public abstract class AbstractCommonService <E extends AbstractEntity, S extends CommonStorage<E>>
        implements CommonService<E> {

    protected final S storage;

    @Autowired
    public AbstractCommonService(S storage) {
        this.storage = storage;
    }

    @Override
    public E create(E entity) {
        E createdEntity = storage.create(entity);
        log.debug("Created entity {} in storage: {}", createdEntity.getClass().getSimpleName(), createdEntity);
        return createdEntity;
    }

    @Override
    public E update(E entity) {
        throwExceptionIfEntityNotExist(entity.getId());
        E updatedEntity = storage.update(entity);
        log.debug("Updated entity {} in storage: {}", updatedEntity.getClass().getSimpleName(), updatedEntity);
        return updatedEntity;
    }

    @Override
    public E delete(long id) {
        throwExceptionIfEntityNotExist(id);
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
        throwExceptionIfEntityNotExist(id);
        return storage.getById(id);
    }

    @Override
    public Collection<E> getAll() {
        return storage.getAll();
    }

    protected void throwExceptionIfEntityNotExist(long id) {
        if (storage.getById(id) == null) {
            log.debug("Entity with id = " + id + " not found.");
            throw new EntityNotFoundException("Entity with id = " + id + " not found.");
        }
    }
}
