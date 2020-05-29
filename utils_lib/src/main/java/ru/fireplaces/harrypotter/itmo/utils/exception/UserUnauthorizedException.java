package ru.fireplaces.harrypotter.itmo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when user is unauthorized or passes bad
 * credentials.
 *
 * @author seniorkot
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserUnauthorizedException extends RuntimeException {

    public UserUnauthorizedException() {
        super();
    }

    public UserUnauthorizedException(String message) {
        super(message);
    }
}
