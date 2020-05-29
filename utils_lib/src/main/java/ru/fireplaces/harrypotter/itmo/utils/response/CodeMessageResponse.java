package ru.fireplaces.harrypotter.itmo.utils.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Custom response entity for HP services controllers.
 *
 * @param <T> Type of {@link CodeMessage} body
 *
 * @author seniorkot
 */
public class CodeMessageResponse<T> extends ResponseEntity<CodeMessage<T>> {

    /**
     * Parametrized constructor for code-message response.<br>
     * Uses 200 actual status code for responses and {@link CodeMessage}
     * as a body.
     *
     * @param code HTTP Status Code
     * @param message Response message
     */
    public CodeMessageResponse(HttpStatus code, T message) {
        super(new CodeMessage<>(code, message), HttpStatus.OK);
    }
}
