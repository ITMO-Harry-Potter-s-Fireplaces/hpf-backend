package ru.fireplaces.harrypotter.itmo.security.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.security.domain.model.User;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
import ru.fireplaces.harrypotter.itmo.utils.exception.InternalServerErrorException;

@Service
public interface SecurityService {

    /**
     * Generates new JWT based on {@link User} data.
     *
     * @param user User object
     * @return Generated JSON Web Token as String
     * @throws InternalServerErrorException Thrown when couldn't generate token
     */
    String generateToken(@NonNull User user) throws InternalServerErrorException;

    /**
     * Validates token for expiration.
     *
     * @param token JSON Web Token value
     * @return true - token is valid, false - token has been expired
     * @throws ActionForbiddenException Thrown when couldn't verify token
     */
    boolean validateTokenExpiration(@NonNull String token) throws ActionForbiddenException;

    /**
     * Authorizes JWT into {@link User}.
     *
     * @param token JSON Web Token value
     * @return Authorized {@link User}
     * @throws ActionForbiddenException Thrown when couldn't authorize user
     */
    User authorizeToken(@NonNull String token) throws ActionForbiddenException;
}
