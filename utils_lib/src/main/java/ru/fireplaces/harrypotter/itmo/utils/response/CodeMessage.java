package ru.fireplaces.harrypotter.itmo.utils.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Custom POJO for responses.
 *
 * @param <T> Message body type
 *
 * @author seniorkot
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeMessage<T> implements Serializable {

    /**
     * Response status code.
     */
    private Integer code;

    /**
     * Response body/message.
     */
    private T message;

    public CodeMessage() {

    }

    /**
     * Parametrized constructor for code-message response.
     *
     * @param code HTTP Status Code
     * @param message Response message
     */
    public CodeMessage(HttpStatus code, T message) {
        this.code = code.value();
        this.message = message;
    }

    /**
     * Parametrized constructor for code-message response.
     *
     * @param code HTTP Status Code value
     * @param message Response message
     */
    public CodeMessage(Integer code, T message) {
        this.code = code;
        this.message = message;
    }
}
