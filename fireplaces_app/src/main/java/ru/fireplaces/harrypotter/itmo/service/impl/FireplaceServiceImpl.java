package ru.fireplaces.harrypotter.itmo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.dao.FireplaceRepository;
import ru.fireplaces.harrypotter.itmo.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.domain.model.request.CoordsRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.service.FireplaceService;
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

    @Autowired
    public FireplaceServiceImpl(FireplaceRepository fireplaceRepository) {
        this.fireplaceRepository = fireplaceRepository;
    }

    @Override
    public Page<Fireplace> getFireplacesPage(@NonNull Pageable pageable,
                                             @NonNull CoordsRequest coords) {
        if (coords.isEmpty()) {
            return fireplaceRepository.findAll(pageable);
        }
        List<String> blankFields = coords.getBlankRequiredFields();
        if (blankFields.size() > 0) {
            throw new BadInputDataException(FireplaceRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        return fireplaceRepository.findAllNearest(pageable, coords.getLat(), coords.getLng(), coords.getRadius());
    }

    @Override
    public Fireplace getFireplace(@NonNull Long id) throws EntityNotFoundException {
        return fireplaceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Fireplace.class, id));
    }

    @Override
    public Fireplace createFireplace(@NonNull FireplaceRequest fireplaceRequest)
            throws BadInputDataException, EntityAlreadyExistsException {
        List<String> blankFields = fireplaceRequest.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(FireplaceRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        if (fireplaceRepository.existsByLatAndLng(fireplaceRequest.getLat(), fireplaceRequest.getLng())) {
            throw new EntityAlreadyExistsException("Fireplace with coords (" + fireplaceRequest.getLat()
                    + ", " + fireplaceRequest.getLng() + ") already exists");
        }
        Fireplace fireplace = new Fireplace();
        fireplace.copy(fireplaceRequest);
        return fireplaceRepository.save(fireplace);
    }

    @Override
    public Fireplace updateFireplace(@NonNull Long id,
                                     @NonNull FireplaceRequest fireplaceRequest,
                                     @NonNull Boolean copy)
            throws EntityNotFoundException, BadInputDataException, EntityAlreadyExistsException {
        Fireplace fireplace = getFireplace(id);
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
        if (fireplaceRepository.existsByLatAndLng(fireplaceRequest.getLat(), fireplaceRequest.getLng())) {
            throw new EntityAlreadyExistsException("Fireplace with coords (" + fireplace.getLng()
                    + ", " + fireplace.getLat() + ") already exists");
        }
        return fireplaceRepository.save(fireplace);
    }

    @Override
    public void deleteFireplace(@NonNull Long id) throws EntityNotFoundException {
        fireplaceRepository.delete(getFireplace(id));
    }
}
