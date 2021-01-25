package ru.fireplaces.harrypotter.itmo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.domain.model.request.CoordsRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityAlreadyExistsException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

/**
 * Service interface to manipulate {@link Fireplace} data.
 *
 * @author seniorkot
 */
@Service
public interface FireplaceService {

    /**
     * Returns a page with {@link Fireplace} entities.<br>
     * Filters used: user latitude and longitude.
     *
     * @param pageable {@link Pageable} params
     * @param coords Latitude and longitude with search radius
     * @return {@link Page} with queered {@link Fireplace} entities
     * @throws BadInputDataException Bad coords
     */
    Page<Fireplace> getFireplacesPage(@NonNull Pageable pageable, @NonNull CoordsRequest coords)
            throws BadInputDataException;

    /**
     * Returns a {@link Fireplace} entity by ID.
     *
     * @param id Fireplace ID
     * @return {@link Fireplace} entity
     * @throws EntityNotFoundException Not Found Exception
     */
    Fireplace getFireplace(@NonNull Long id) throws EntityNotFoundException;

    /**
     * Creates new {@link Fireplace} entity.
     *
     * @param fireplaceRequest Fireplace params
     * @return Created {@link Fireplace} entity
     * @throws BadInputDataException Bad input data
     * @throws EntityAlreadyExistsException {@link Fireplace} with such lat and lng already exists
     */
    Fireplace createFireplace(@NonNull FireplaceRequest fireplaceRequest)
            throws BadInputDataException, EntityAlreadyExistsException;

    /**
     * Updates {@link Fireplace} entity by ID.
     *
     * @param id Fireplace ID
     * @param fireplaceRequest Fireplace params
     * @param copy Copy or update
     * @return Updated {@link Fireplace} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws BadInputDataException Bad input data
     * @throws EntityAlreadyExistsException {@link Fireplace} with such lat and lng already exists
     */
    Fireplace updateFireplace(@NonNull Long id, @NonNull FireplaceRequest fireplaceRequest, @NonNull Boolean copy)
            throws EntityNotFoundException, BadInputDataException, EntityAlreadyExistsException;

    /**
     * Deletes {@link Fireplace} entity by ID.
     *
     * @param id Fireplace ID
     * @throws EntityNotFoundException Not Found Exception
     */
    void deleteFireplace(@NonNull Long id) throws EntityNotFoundException;
}
