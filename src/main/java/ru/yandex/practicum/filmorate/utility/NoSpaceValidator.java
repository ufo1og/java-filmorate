package ru.yandex.practicum.filmorate.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NoSpaceValidator implements ConstraintValidator<NoSpace, String> {

    public void initialize(BeforeNow annotation) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (value.contains(" ")) {
                valid = false;
            }
        }
        return valid;
    }
}
