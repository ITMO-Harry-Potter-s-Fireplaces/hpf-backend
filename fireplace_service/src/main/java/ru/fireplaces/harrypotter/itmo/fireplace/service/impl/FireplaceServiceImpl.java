package ru.fireplaces.harrypotter.itmo.fireplace.service.impl;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.dao.FireplaceRepository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.CoordsRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.response.User;
import ru.fireplaces.harrypotter.itmo.fireplace.feign.SecurityApiClient;
import ru.fireplaces.harrypotter.itmo.fireplace.service.FireplaceService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityAlreadyExistsException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

import java.util.List;

/**
 * Implementation of {@link FireplaceService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = FireplaceServiceImpl.SERVICE_VALUE)
public class FireplaceServiceImpl implements FireplaceService {

    public static final String SERVICE_VALUE = "FireplaceServiceImpl";

    private final FireplaceRepository fireplaceRepository;
    private final SecurityApiClient securityApiClient;

    @Autowired
    public FireplaceServiceImpl(FireplaceRepository fireplaceRepository,
                                SecurityApiClient securityApiClient) {
        this.fireplaceRepository = fireplaceRepository;
        this.securityApiClient = securityApiClient;
    }

    @Override
    public Page<Fireplace> getFireplacesPage(@NonNull Pageable pageable,
                                             @Nullable CoordsRequest coords) {
        if (coords == null) {
            return fireplaceRepository.findAll(pageable)
                    .map(fireplace -> {
                        fireplace.setOwner(securityApiClient
                                .getUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN), fireplace.getOwnerId()).getMessage());
                        return fireplace;
                    });
        }
        List<String> blankFields = coords.getBlankRequiredFields();
        if (blankFields.size() > 0) {
            throw new BadInputDataException(FireplaceRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        return fireplaceRepository.findAllNearest(pageable, coords.getLng(), coords.getLat())
                .map(fireplace -> {
                    fireplace.setOwner(securityApiClient
                            .getUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN), fireplace.getOwnerId()).getMessage());
            return fireplace;
        });
    }

    @Override
    public Fireplace getFireplace(@NonNull Long id) throws EntityNotFoundException {
        return fireplaceRepository.findById(id)
                .map(fireplace -> {
                    fireplace.setOwner(securityApiClient
                            .getUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN), fireplace.getOwnerId()).getMessage());
                    return fireplace;
                })
                .orElseThrow(() -> new EntityNotFoundException(Fireplace.class, id));
    }

    @Override
    public Fireplace createFireplace(@NonNull FireplaceRequest fireplaceRequest)
            throws BadInputDataException, EntityAlreadyExistsException {
        List<String> blankFields = fireplaceRequest.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(FireplaceRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        if (fireplaceRepository.existsByLngAndLat(fireplaceRequest.getLng(), fireplaceRequest.getLat())) {
            throw new EntityAlreadyExistsException("Fireplace with coords (" + fireplaceRequest.getLng()
                    + ", " + fireplaceRequest.getLat() + ") already exists");
        }
        User currentUser = securityApiClient.getCurrentUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN)).getMessage();
        Fireplace fireplace = new Fireplace();
        fireplace.copy(fireplaceRequest);
        fireplace.setOwnerId(currentUser.getId());
        return fireplaceRepository.save(fireplace);
    }

    @Override
    public Fireplace updateFireplace(@NonNull Long id,
                                     @NonNull FireplaceRequest fireplaceRequest,
                                     @NonNull Boolean copy)
            throws EntityNotFoundException, BadInputDataException, ActionForbiddenException {
        User currentUser = securityApiClient.getCurrentUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN)).getMessage();
        Fireplace fireplace = getFireplace(id);
        if (!fireplace.getOwnerId().equals(currentUser.getId())) {
            throw new ActionForbiddenException("Not enough permissions to update fireplace");
        }
        if (copy) {
            List<String> blankFields = fireplaceRequest.getBlankRequiredFields(); // Get blank fields
            if (blankFields.size() > 0) {
                throw new BadInputDataException(FireplaceRequest.class,
                        String.join(", ", blankFields), "are missing");
            }
            fireplace.copy(fireplaceRequest);
        }
        else {
            fireplace.update(fireplaceRequest);
        }
        if (fireplaceRepository.existsByLngAndLat(fireplace.getLng(), fireplace.getLat())) {
            throw new EntityAlreadyExistsException("Fireplace with coords (" + fireplace.getLng()
                    + ", " + fireplace.getLat() + ") already exists");
        }
        return fireplaceRepository.save(fireplace);
    }

    @Override
    public void deleteFireplace(@NonNull Long id) throws EntityNotFoundException, ActionForbiddenException {
        User currentUser = securityApiClient.getCurrentUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN)).getMessage();
        Fireplace fireplace = getFireplace(id);
        if (!fireplace.getOwnerId().equals(currentUser.getId())
                || !currentUser.getRole().equals(Role.MODERATOR)
                && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new ActionForbiddenException("Not enough permissions to delete fireplace");
        }
        fireplaceRepository.delete(fireplace);
    }
}
