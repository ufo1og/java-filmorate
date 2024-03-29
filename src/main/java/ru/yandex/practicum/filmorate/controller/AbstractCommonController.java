package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.service.CommonService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class AbstractCommonController <E extends AbstractEntity, S extends CommonService<E>>
        implements CommonController<E> {

    protected final S service;

    @Override
    public E post(@RequestBody @Valid E entity) {
        E e = service.create(entity);
        log.debug("Created {}: {}", e.getClass().getSimpleName(), e);
        return e;
    }

    @Override
    public E put(@RequestBody @Valid E entity) {
        E e = service.update(entity);
        log.debug("Updated {}: {}", e.getClass().getSimpleName(), e);
        return e;
    }

    @Override
    public E getEntity(@PathVariable("id") long id) {
        return service.getById(id);
    }

    @Override
    public Collection<E> getAll() {
        return service.getAll();
    }
}
