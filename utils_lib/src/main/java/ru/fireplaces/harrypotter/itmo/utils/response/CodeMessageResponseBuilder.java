package ru.fireplaces.harrypotter.itmo.utils.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.net.URI;

/**
 * Util to return {@link CodeMessageResponse} with {@link CodeMessage}
 * as body with actual information about response.
 *
 * @author seniorkot
 */
public class CodeMessageResponseBuilder {

    /**
     * Returns {@link PageBody} with 200 response code.
     *
     * @param body Response body page
     * @param <T> Page item type
     * @return {@link PageResponse} with {@link PageBody} as body
     */
    public static <T> PageResponse<T> page(Page<T> body) {
        return new PageResponse<>(body);
    }

    /**
     * Returns {@link CodeMessage} with 200 response code.
     *
     * @param body Response body
     * @param <T> Response body type
     * @return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static <T> CodeMessageResponse<T> ok(T body) {
        return new CodeMessageResponse<>(HttpStatus.OK, body);
    }

    /**
     * Returns {@link CodeMessage} with 201 response code.
     *
     * @param location {@link URI} location of created object
     * @return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<URI> created(URI location) {
        return new CodeMessageResponse<>(HttpStatus.CREATED, location);
    }

    /**
     * Returns {@link CodeMessage} with 204 response code.
     *
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> noContent() {
        return new CodeMessageResponse<>(HttpStatus.NO_CONTENT, "");
    }

    /**
     * Returns {@link CodeMessage} with 400 response code.
     *
     * @param message Error message
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> badRequest(String message) {
        return new CodeMessageResponse<>(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Returns {@link CodeMessage} with 401 response code.
     *
     * @param message Error message
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> unauthorized(String message) {
        return new CodeMessageResponse<>(HttpStatus.UNAUTHORIZED, message);
    }

    /**
     * Returns {@link CodeMessage} with 403 response code.
     *
     * @param message Error message
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> forbidden(String message) {
        return new CodeMessageResponse<>(HttpStatus.FORBIDDEN, message);
    }

    /**
     * Returns {@link CodeMessage} with 404 response code.
     *
     * @param message Error message
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> notFound(String message) {
        return new CodeMessageResponse<>(HttpStatus.NOT_FOUND, message);
    }

    /**
     * Returns {@link CodeMessage} with 409 response code.
     *
     * @param message Error message
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> conflict(String message) {
        return new CodeMessageResponse<>(HttpStatus.CONFLICT, message);
    }

    /**
     * Returns {@link CodeMessage} with 422 response code.
     *
     * @param message Error message
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> unprocessable(String message) {
        return new CodeMessageResponse<>(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    /**
     * Returns {@link CodeMessage} with 500 response code.
     *
     * @param message Error message
     * >@return {@link CodeMessageResponse} with {@link CodeMessage} as body
     */
    public static CodeMessageResponse<String> internal(String message) {
        return new CodeMessageResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
