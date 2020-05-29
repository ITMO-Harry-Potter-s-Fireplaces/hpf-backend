package ru.fireplaces.harrypotter.itmo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when POSTing data already exists.
 *
 * @author seniorkot
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException() {
        super();
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
