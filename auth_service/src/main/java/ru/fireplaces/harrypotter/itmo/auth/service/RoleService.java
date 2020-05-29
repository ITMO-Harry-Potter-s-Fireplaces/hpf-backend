package ru.fireplaces.harrypotter.itmo.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.Role;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.RoleRequest;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityAlreadyExistsException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

import java.util.List;

/**
 * Service interface with methods to manipulate {@link Role} data.
 *
 * @author seniorkot
 */
@Service
public interface RoleService {

    /**
     * Returns a page of {@link Role} entities.
     *
     * @param pageable Pageable params
     * @return {@link Page} with queered {@link Role} entities
     */
    Page<Role> getRolesPage(@NonNull Pageable pageable);

    /**
     * Returns a list of {@link Role} entities.
     *
     * @return {@link List} with queered {@link Role} entities
     */
    List<Role> getRoles();

    /**
     * Returns a {@link Role} entity by ID.
     *
     * @param id Role ID
     * @return {@link Role} entity
     * @throws EntityNotFoundException Not found exception
     */
    Role getRole(@NonNull Long id) throws EntityNotFoundException;

    /**
     * Creates new {@link Role} entity, adds it to DB and returns created object.
     *
     * @param newRole Role params
     * @return Created {@link Role}
     * @throws BadInputDataException Bad {@link RoleRequest} properties exception
     * @throws EntityAlreadyExistsException Role with such name already exists
     */
    Role addRole(@NonNull RoleRequest newRole) throws BadInputDataException, EntityAlreadyExistsException;

    /**
     * Updates existing {@link Role} entity by ID and returns updated object.
     *
     * @param id Role ID
     * @param newRole Role params
     * @return Updated {@link Role}
     * @throws BadInputDataException Bad {@link RoleRequest} properties exception
     * @throws EntityNotFoundException Not Found Exception
     * @throws EntityAlreadyExistsException Role with such name already exists
     */
    Role updateRole(@NonNull Long id, @NonNull RoleRequest newRole)
            throws BadInputDataException, EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Deletes existing {@link Role} entity by ID.
     *
     * @param id Role ID
     * @throws EntityNotFoundException Not Found Exception
     */
    void deleteRole(@NonNull Long id) throws EntityNotFoundException;
}
