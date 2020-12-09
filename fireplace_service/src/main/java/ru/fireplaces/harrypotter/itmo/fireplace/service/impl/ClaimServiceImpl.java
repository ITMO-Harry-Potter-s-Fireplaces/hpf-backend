package ru.fireplaces.harrypotter.itmo.fireplace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.dao.ClaimRepository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.service.ClaimService;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionInapplicableException;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

/**
 * Implementation of {@link ClaimService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = ClaimServiceImpl.SERVICE_VALUE)
public class ClaimServiceImpl implements ClaimService {

    public static final String SERVICE_VALUE = "ClaimServiceImpl";

    private final ClaimRepository claimRepository;

    @Autowired
    public ClaimServiceImpl(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Override
    public Page<Claim> getClaimsPage(@NonNull Pageable pageable,
                                     @Nullable ClaimStatus status,
                                     @Nullable Long userId) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Page<Claim> getCurrentClaimsPage(@NonNull Pageable pageable,
                                            @Nullable ClaimStatus status) {
        return null;
    }

    @Override
    public Claim getClaim(@NonNull Long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Claim createClaim(@NonNull ClaimRequest request) throws BadInputDataException {
        return null;
    }

    @Override
    public Claim approveClaim(@NonNull Long id, @NonNull Boolean approve)
            throws EntityNotFoundException, ActionInapplicableException {
        return null;
    }

    @Override
    public Claim completeClaim(@NonNull Long id, @NonNull Boolean cancel)
            throws EntityNotFoundException, ActionInapplicableException {
        return null;
    }
}
