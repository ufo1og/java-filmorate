package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.storage.CommonStorage;

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
        if (storage.getById(entity.getId()) == null) {
            //TODO: ошибка 404
        }
        return storage.update(entity);
    }

    @Override
    public E delete(long id) {
        if (storage.getById(id) == null) {
            //TODO: ошибка 404
        }
        return storage.delete(id);
    }

    @Override
    public void deleteAll() {
        storage.deleteAll();
    }

    @Override
    public E getById(long id) {
        E entity = storage.getById(id);
        if (entity == null) {
            //TODO: ошибка 404
        }
        return entity;
    }

    @Override
    public Collection<E> getAll() {
        return storage.getAll();
    }
}
