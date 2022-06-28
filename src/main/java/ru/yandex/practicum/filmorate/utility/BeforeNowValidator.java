package ru.yandex.practicum.filmorate.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BeforeNowValidator implements ConstraintValidator<BeforeNow, LocalDate> {

    private LocalDate now;

    public void initialize(BeforeNow annotation) {
      now = LocalDate.now();
    }

    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (!value.isBefore(now)) {
                valid = false;
            }
        }
        return valid;
    }
}
