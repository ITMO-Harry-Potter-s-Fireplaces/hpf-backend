package ru.fireplaces.harrypotter.itmo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.model.AuthLog;
import ru.fireplaces.harrypotter.itmo.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.response.LoginResponse;
import ru.fireplaces.harrypotter.itmo.utils.exception.*;

import javax.servlet.http.HttpServletRequest;

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
     * @param userRequest Login, password and user data
     * @param request Request object
     * @return ID of created {@link ru.fireplaces.harrypotter.itmo.domain.model.User}
     *      and generated JSON Web Token
     * @throws EntityAlreadyExistsException User already exists
     * @throws BadInputDataException Login or password are missed
     */
    LoginResponse register(@NonNull UserRequest userRequest,
                           HttpServletRequest request)
            throws EntityAlreadyExistsException, BadInputDataException;

    /**
     * Authenticates user in the system.
     *
     * @param loginRequest Login and password
     * @param request Request object
     * @return {@link ru.fireplaces.harrypotter.itmo.domain.model.User} ID and
     *      generated JSON Web Token
     * @throws EntityNotFoundException User doesn't exist
     * @throws BadInputDataException Login or password are missed
     * @throws UserUnauthorizedException Invalid login or password
     * @throws ActionForbiddenException User is banned
     */
    LoginResponse login(@NonNull LoginRequest loginRequest,
                        HttpServletRequest request) throws EntityNotFoundException,
            BadInputDataException, UserUnauthorizedException, ActionForbiddenException;

    /**
     * Returns a page with {@link AuthLog} entities by user ID.
     *
     * @param pageable Pageable params
     * @param id User ID.
     * @return {@link Page} with queered {@link AuthLog} entities
     * @throws EntityNotFoundException User with such ID not found
     */
    Page<AuthLog> getAuthHistory(@NonNull Pageable pageable,
                                 @NonNull Long id) throws EntityNotFoundException;

    /**
     * Returns a page with {@link AuthLog} entities of current user.
     *
     * @param pageable Pageable params
     * @param token Auth token
     * @return {@link Page} with queered {@link AuthLog} entities
     */
    Page<AuthLog> getCurrentAuthHistory(@NonNull Pageable pageable,
                                        @NonNull String token);
}
