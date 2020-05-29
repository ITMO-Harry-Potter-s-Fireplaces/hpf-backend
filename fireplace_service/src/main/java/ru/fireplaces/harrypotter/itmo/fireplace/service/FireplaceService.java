package ru.fireplaces.harrypotter.itmo.fireplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.FireplaceRequest;
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
     * Filters used: latitude and longitude.
     *
     * @param pageable {@link Pageable} params
     * @param fireplaceParams {@link FireplaceRequest} with latitude and longitude
     * @return {@link Page} with queered {@link Fireplace} entities
     */
    Page<Fireplace> getFireplacesPage(@NonNull Pageable pageable,
                                      FireplaceRequest fireplaceParams);

    /**
     * Returns a {@link Fireplace} entity by ID.
     *
     * @param id Fireplace ID
     * @return {@link Fireplace} entity
     * @throws EntityNotFoundException Not Found Exception
     */
    Fireplace getFireplace(@NonNull Long id) throws EntityNotFoundException;
}
