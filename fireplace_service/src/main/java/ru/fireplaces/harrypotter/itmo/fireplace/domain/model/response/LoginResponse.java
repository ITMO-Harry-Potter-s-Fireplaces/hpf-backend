package ru.fireplaces.harrypotter.itmo.fireplace.domain.model.response;

import lombok.Data;

@Data
public class LoginResponse {

    /**
     * User ID.
     */
    private Long id;

    /**
     * Auth token.
     */
    private String token;
}
