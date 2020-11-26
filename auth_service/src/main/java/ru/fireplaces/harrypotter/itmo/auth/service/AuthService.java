package ru.fireplaces.harrypotter.itmo.auth.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.response.LoginResponse;
import ru.fireplaces.harrypotter.itmo.utils.exception.*;

/**
 * Service interface with methods to sign up and sign in.
 *
 * @author seniorkot
 */
@Service
public interface AuthService {

    /**
     * Registers new user in the system.
     *
     * @param request Login and password
     * @return ID of created {@link ru.fireplaces.harrypotter.itmo.auth.domain.model.User}
     *      and generated JSON Web Token
     * @throws EntityAlreadyExistsException User already exists
     * @throws BadInputDataException Login or password are missed
     */
    LoginResponse register(@NonNull UserRequest request)
            throws EntityAlreadyExistsException, BadInputDataException;

    /**
     * Authenticates user in the system.
     *
     * @param request Login and password
     * @return {@link ru.fireplaces.harrypotter.itmo.auth.domain.model.User} ID and
     *      generated JSON Web Token
     * @throws EntityNotFoundException User doesn't exist
     * @throws BadInputDataException Login or password are missed
     * @throws UserUnauthorizedException Invalid login or password
     * @throws ActionForbiddenException User is banned
     */
    LoginResponse login(@NonNull LoginRequest request) throws EntityNotFoundException,
            BadInputDataException, UserUnauthorizedException, ActionForbiddenException;
}
