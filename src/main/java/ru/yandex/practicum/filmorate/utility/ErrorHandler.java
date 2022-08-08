package ru.yandex.practicum.filmorate.utility;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.utility.exceptions.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse EntityNotFoundHandler(final EntityNotFoundException e) {
        log.debug(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse ServerErrorHandler(final Throwable e) {
        log.warn("Response code 500 because of '{}'", e.getClass().getSimpleName(), e);
        return new ErrorResponse(e.getMessage());
    }
}

@Data
class ErrorResponse {
    final String error;
}
