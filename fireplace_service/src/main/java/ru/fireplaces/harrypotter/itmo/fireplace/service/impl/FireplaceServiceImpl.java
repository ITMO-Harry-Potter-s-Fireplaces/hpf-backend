package ru.fireplaces.harrypotter.itmo.fireplace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.dao.FireplaceRepository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.service.FireplaceService;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
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
                                             FireplaceRequest fireplaceParams) {
        if (fireplaceParams == null) {
            return fireplaceRepository.findAll(pageable);
        }
        List<String> blankFields = fireplaceParams.getBlankRequiredFields();
        if (blankFields.size() > 0) {
            throw new BadInputDataException(FireplaceRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        return fireplaceRepository.findAllNearest(pageable, fireplaceParams.getLng(), fireplaceParams.getLat());
    }

    @Override
    public Fireplace getFireplace(@NonNull Long id) throws EntityNotFoundException {
        return fireplaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Fireplace.class, id));
    }
}
