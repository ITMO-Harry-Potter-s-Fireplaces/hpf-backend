package ru.fireplaces.harrypotter.itmo.utils.interfaces.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Interface with method to copy request params to model entity.
 *
 * @param <T> Model Request class
 *
 * @author seniorkot
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public interface CopyFromRequest<T> extends Serializable {

    /**
     * Copies request params to model.
     *
     * @param request Request params.
     */
    void copy(T request);

    /**
     * Updates model params from request.<br>
     * Null fields should be ignored.
     *
     * @param request Request params
     */
    void update(T request);

}
