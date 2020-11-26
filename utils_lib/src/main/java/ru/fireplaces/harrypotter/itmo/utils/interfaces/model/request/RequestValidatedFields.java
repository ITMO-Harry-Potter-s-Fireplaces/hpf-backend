package ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Interface for validating fields in request POJOs.
 *
 * @author seniorkot
 */
public interface RequestValidatedFields {

    /**
     * Validates fields in request POJO.
     *
     * @return {@link List} of error messages.
     */
    @JsonIgnore
    List<String> getValidationErrorMessages();
}
