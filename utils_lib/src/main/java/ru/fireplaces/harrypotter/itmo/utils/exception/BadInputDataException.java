package ru.fireplaces.harrypotter.itmo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when input data is bad.
 *
 * @author seniorkot
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadInputDataException extends RuntimeException {

    public BadInputDataException() {
        super();
    }

    public BadInputDataException(String message) {
        super(message);
    }

    public BadInputDataException(Class<?> classType, String fieldName, String message) {
        super(fieldName + " of " + classType.getSimpleName() + " " + message);
    }
}
