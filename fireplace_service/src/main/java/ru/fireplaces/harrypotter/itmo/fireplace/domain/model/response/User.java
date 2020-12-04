package ru.fireplaces.harrypotter.itmo.fireplace.domain.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * User data POJO. Fetched from other services.
 *
 * @author seniorkot
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    /**
     * User ID.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    /**
     * User name.
     */
    private String name;

    /**
     * User surname.
     */
    private String surname;

    /**
     * User middle name.
     */
    private String middleName;

    /**
     * Is user active.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean active;
}