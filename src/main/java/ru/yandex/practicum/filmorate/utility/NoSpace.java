package ru.yandex.practicum.filmorate.utility;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoSpaceValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpace {
    String message() default "must not contain any space characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}