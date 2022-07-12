package ru.yandex.practicum.filmorate.utility;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse EntityNotFoundHandler(final EntityNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}

@Data
class ErrorResponse {
    final String error;
}
