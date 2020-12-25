package ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request;

import lombok.Data;

@Data
public class LoginRequest {

    /**
     * User email.
     */
    private String email;

    /**
     * User password.
     */
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
