package ru.fireplaces.harrypotter.itmo.fireplace.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionInapplicableException;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

/**
 * Service interface to manipulate {@link Claim} data.
 *
 * @author seniorkot
 */
@Service
public interface ClaimService {

    /**
     * Returns a page with {@link Claim} entities.<br>
     * Filters used: claim status and user ID.
     *
     * @param pageable Pageable params
     * @param status Claim status
     * @param userId User ID
     * @return {@link Page} with queered {@link Claim} entities
     * @throws EntityNotFoundException User with such ID is not found
     */
    Page<Claim> getClaimsPage(@NonNull Pageable pageable, @Nullable ClaimStatus status, @Nullable Long userId)
            throws EntityNotFoundException;

    /**
     * Returns a page with {@link Claim} entities of current user.<br>
     * Filters used: claim status.
     *
     * @param pageable Pageable params
     * @param status Claim status
     * @return {@link Page} with queered {@link Claim} entities
     */
    Page<Claim> getCurrentClaimsPage(@NonNull Pageable pageable, @Nullable ClaimStatus status);

    /**
     * Returns a {@link Claim} entity by ID.
     *
     * @param id Claim ID
     * @return {@link Claim} entity
     * @throws EntityNotFoundException Not Found Exception
     */
    Claim getClaim(@NonNull Long id) throws EntityNotFoundException;

    /**
     * Creates new {@link Claim} entity.
     *
     * @param request Claim params
     * @return Created {@link Claim} entity
     * @throws BadInputDataException Bad input data
     */
    Claim createClaim(@NonNull ClaimRequest request) throws BadInputDataException;

    /**
     * Updates {@link Claim} status to APPROVED or REJECTED by ID.
     *
     * @param id Claim ID
     * @param approve Approve or reject
     * @return Updated {@link Claim} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws ActionInapplicableException Cannot change claim status
     */
    Claim approveClaim(@NonNull Long id, @NonNull Boolean approve)
            throws EntityNotFoundException, ActionInapplicableException;

    /**
     * Updates {@link Claim} status to COMPLETED or CANCELLED by ID.
     *
     * @param id Claim ID
     * @param cancel Cancel or complete
     * @return Updated {@link Claim} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws ActionInapplicableException Cannot change claim status
     */
    Claim completeClaim(@NonNull Long id, @NonNull Boolean cancel)
            throws EntityNotFoundException, ActionInapplicableException;
}
