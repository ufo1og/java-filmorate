package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.utility.After;

import javax.management.ConstructorParameters;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    @Size(max = 200, message = "{validation.name.size.too_long}")
    private final String description;

    @NotNull
    @After("1895-12-28")
    private final LocalDate releaseDate;

    @Min(0)
    private final long duration; // в минутах
}
