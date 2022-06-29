package ru.yandex.practicum.filmorate.utility;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmValidator implements ConstraintValidator<ValidFilm, Film> {

    private LocalDate borderDate;

    @Override
    public void initialize(ValidFilm constraintAnnotation) {
        borderDate = LocalDate.of(1895, 12, 28);
    }

    @Override
    public boolean isValid(Film film, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = film.getId() >= 0;
        if (film.getName() == null || "".equals(film.getName())) {
            isValid = false;
        }
        if (film.getDescription() == null || "".equals(film.getDescription()) || film.getDescription().length() > 200) {
            isValid = false;
        }
        if (film.getReleaseDate() == null || !film.getReleaseDate().isAfter(borderDate)) {
            isValid = false;
        }
        if (film.getDuration() < 0) {
            isValid = false;
        }
        return isValid;
    }
}
