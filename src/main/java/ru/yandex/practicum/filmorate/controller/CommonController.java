package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import javax.validation.Valid;
import java.util.Collection;

public interface CommonController <E extends AbstractEntity> {
    @PostMapping
    E post(@RequestBody @Valid E entity);

    @PutMapping
    E put(@RequestBody @Valid E entity);

    @GetMapping("/{id}")
    E getEntity(@PathVariable("id") long id);

    @GetMapping
    Collection<E> getAll();
}
