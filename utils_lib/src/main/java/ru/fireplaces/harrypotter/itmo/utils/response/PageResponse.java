package ru.fireplaces.harrypotter.itmo.utils.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

/**
 * Custom response entity for HP services controllers.<br>
 * Used for paginated responses.
 *
 * @param <T> Type of page body
 *
 * @author seniorkot
 */
public class PageResponse<T> extends CodeMessageResponse<PageBody<T>> {

    /**
     * Parametrized constructor for pageable response.<br>
     * Uses {@link CodeMessageResponse} to form successful (200)
     * response with {@link PageBody} as response body.
     *
     * @param page Page to display
     */
    public PageResponse(Page<T> page) {
        super(HttpStatus.OK, new PageBody<>(page));
    }
}
