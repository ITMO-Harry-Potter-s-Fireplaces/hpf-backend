package ru.fireplaces.harrypotter.itmo.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
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
     * @return {@link Page} with queered {@link User} entities
     */
    Page<User> getUsersPage(@NonNull Pageable pageable,
                            List<Long> roleIds);

    /**
     * Returns a list of {@link User} entities.<br>
     * Filters used: {@link User} role IDs.
     *
     * @param roleIds List of role IDs
     * @return {@link List} with queered {@link User} entities
     */
    List<User> getUsers(List<Long> roleIds);

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
     * @param newUser User params
     * @return Updated {@link User}
     * @throws BadInputDataException Bad {@link UserRequest} properties exception
     * @throws EntityNotFoundException Not Found Exception
     * @throws EntityAlreadyExistsException User with such email already exists
     */
    User updateUser(@NonNull Long id, @NonNull UserRequest newUser)
            throws BadInputDataException, EntityNotFoundException, EntityAlreadyExistsException;
}
