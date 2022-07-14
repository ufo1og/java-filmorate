package ru.yandex.practicum.filmorate.utility;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.utility.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserValidator implements ConstraintValidator<ValidUser, User> {
    private LocalDate now;
    private Pattern validEmailRegex;

    @Override
    public void initialize(ValidUser constraintAnnotation) {
        now = LocalDate.now();
        validEmailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;
        if (user.getEmail() == null || "".equals(user.getEmail()) || !emailValidation(user.getEmail())) {
            isValid = false;
        }
        if (user.getLogin() == null || "".equals(user.getLogin()) || user.getLogin().contains(" ")) {
            isValid = false;
        }
        if (user.getBirthday() == null || !user.getBirthday().isBefore(now)) {
            isValid = false;
        }
        return isValid;
    }

    private boolean emailValidation(String email) {
        return validEmailRegex.matcher(email).find();
    }
}
