package ru.fireplaces.harrypotter.itmo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when action cannot be done due to forbidden access.
 *
 * @author seniorkot
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ActionForbiddenException extends RuntimeException {

    public ActionForbiddenException() {
        super();
    }

    public ActionForbiddenException(String message) {
        super(message);
    }
}
