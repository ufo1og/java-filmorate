package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.storage.CommonStorage;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

import java.util.Collection;

public abstract class AbstractCommonService <E extends AbstractEntity, S extends CommonStorage<E>>
        implements CommonService<E> {

    protected final S storage;

    @Autowired
    public AbstractCommonService(S storage) {
        this.storage = storage;
    }

    @Override
    public E create(E entity) {
        return storage.create(entity);
    }

    @Override
    public E update(E entity) {
        throwExceptionIfEntityNotExist(entity.getId());
        return storage.update(entity);
    }

    @Override
    public E delete(long id) {
        throwExceptionIfEntityNotExist(id);
        return storage.delete(id);
    }

    @Override
    public void deleteAll() {
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
            throw new EntityNotFoundException("Entity with id = " + id + " not found.");
        }
    }
}
