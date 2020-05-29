package ru.fireplaces.harrypotter.itmo.auth.domain.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

/**
 * Login response with {@link ru.fireplaces.harrypotter.itmo.auth.domain.model.User} ID
 * and generated JWT.
 *
 * @author seniorkot
 */
@Data
public class LoginResponse implements Serializable {

    /**
     * User ID.
     */
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * JSON Web Token value.
     */
    @Setter(AccessLevel.NONE)
    private String token;

    public LoginResponse(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
