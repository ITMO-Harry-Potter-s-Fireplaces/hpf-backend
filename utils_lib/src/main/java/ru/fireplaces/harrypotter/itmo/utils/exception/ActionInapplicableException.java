package ru.fireplaces.harrypotter.itmo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when action cannot be done due to some logic rules.
 *
 * @author seniorkot
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ActionInapplicableException extends RuntimeException {

    public ActionInapplicableException() {
        super();
    }

    public ActionInapplicableException(String message) {
        super(message);
    }
}
