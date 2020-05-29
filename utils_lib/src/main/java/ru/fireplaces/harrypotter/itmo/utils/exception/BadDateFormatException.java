package ru.fireplaces.harrypotter.itmo.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * An exception thrown when input date has incorrect format.
 *
 * @author seniorkot
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadDateFormatException extends RuntimeException {

    public BadDateFormatException() {
        super();
    }

    public BadDateFormatException(String inputValue, List<String> dateFormats) {
        super(inputValue + " doesn't match any date pattern: " + dateFormats.toString());
    }
}
