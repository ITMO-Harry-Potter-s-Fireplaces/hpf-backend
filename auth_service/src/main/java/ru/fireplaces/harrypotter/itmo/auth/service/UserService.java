package ru.fireplaces.harrypotter.itmo.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityAlreadyExistsException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

import java.util.List;

/**
 * Service interface to manipulate {@link User} data.
 *
 * @author seniorkot
 */
@Service
public interface UserService {

    /**
     * Returns a page with {@link User} entities.<br>
     * Filters used: {@link User} role IDs.
     *
     * @param pageable {@link Pageable} params
     * @param roleIds List of role IDs
     * @param active Account status
     * @return {@link Page} with queered {@link User} entities
     */
    Page<User> getUsersPage(@NonNull Pageable pageable,
                            List<Long> roleIds,
                            @NonNull Boolean active);

    /**
     * Returns a list of {@link User} entities.<br>
     * Filters used: {@link User} role IDs.
     *
     * @param roleIds List of role IDs
     * @param active Account status
     * @return {@link List} with queered {@link User} entities
     */
    List<User> getUsers(List<Long> roleIds, @NonNull Boolean active);

    /**
     * Returns a {@link User} entity by ID.
     *
     * @param id User ID
     * @return {@link User} entity
     * @throws EntityNotFoundException Not Found Exception
     */
    User getUser(@NonNull Long id) throws EntityNotFoundException;

    /**
     * Updates existing {@link User} entity by ID and returns updated object.
     *
     * @param id User ID
     * @param userRequest User params
     * @param copy Copy or ignore null fields
     * @return Updated {@link User}
     * @throws BadInputDataException Bad {@link UserRequest} properties exception
     * @throws EntityNotFoundException Not Found Exception
     * @throws EntityAlreadyExistsException User with such email already exists
     */
    User updateUser(@NonNull Long id, @NonNull UserRequest userRequest, @NonNull Boolean copy)
            throws BadInputDataException, EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Deletes existing {@link User} entity by ID.
     *
     * @param id User ID
     * @throws EntityNotFoundException Not Found Exception
     */
    void deleteUser(@NonNull Long id) throws EntityNotFoundException;

    /**
     * Returns current {@link User} entity.
     *
     * @return {@link User} entity
     */
    User getCurrentUser();

    /**
     * Updates current {@link User} entity.
     *
     * @param userRequest User params
     * @param copy Copy or ignore null fields
     * @return Updated {@link User}
     * @throws BadInputDataException Bad {@link UserRequest} properties exception
     * @throws EntityAlreadyExistsException User with such email already exists
     */
    User updateCurrentUser(@NonNull UserRequest userRequest, @NonNull Boolean copy)
            throws BadInputDataException, EntityAlreadyExistsException;

    /**
     * Deletes current {@link User} entity.
     */
    void deleteCurrentUser();

    /**
     * Changes {@link User} entity {@link Role} by ID.
     *
     * @param id User ID
     * @param role New user role
     * @return Updated {@link User} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws ActionForbiddenException Action is not allowed
     */
    User changeUserRole(@NonNull Long id, @NonNull Role role)
            throws EntityNotFoundException, ActionForbiddenException;
}
