package ru.fireplaces.harrypotter.itmo.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.model.User;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;

import java.util.List;

/**
 * This interface describes methods to check user permissions
 * and verify JSON Web Tokens.
 *
 * @author seniorkot
 */
@Service
public interface PermissionsService {

    /**
     * Verifies if {@link User} is allowed to do some action.
     *
     * @param token Authorization token
     * @param roles Roles list
     * @return true - user is allowed
     * @throws ActionForbiddenException Token is not valid
     */
    boolean userHasRole(@NonNull String token,
                        @NonNull List<Role> roles) throws ActionForbiddenException;

    /**
     * Verifies Auth token (for expiration)
     * and returns bool value as result.
     *
     * @param token Authorization token
     * @return true - token is valid
     * @throws ActionForbiddenException Thrown when token is incorrect
     */
    boolean verifyToken(@NonNull String token) throws ActionForbiddenException;
}
