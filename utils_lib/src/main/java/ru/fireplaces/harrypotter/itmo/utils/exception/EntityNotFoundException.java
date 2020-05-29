package ru.fireplaces.harrypotter.itmo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when requested entity couldn't be found.
 *
 * @author seniorkot
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Class<?> classType, Long id) {
        super("No " + classType.getSimpleName() + " with ID " + id + " found");
    }

}
