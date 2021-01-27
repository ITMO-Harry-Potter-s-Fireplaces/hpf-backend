package ru.fireplaces.harrypotter.itmo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.domain.model.ClaimReport;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimReportRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
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
     * @param include Include status
     * @param userId User ID
     * @return {@link Page} with queered {@link Claim} entities
     * @throws EntityNotFoundException User with such ID is not found
     */
    Page<Claim> getClaimsPage(@NonNull Pageable pageable,
                              @Nullable ClaimStatus status,
                              @NonNull Boolean include,
                              @Nullable Long userId)
            throws EntityNotFoundException;

    /**
     * Returns a page with {@link Claim} entities of current user.<br>
     * Filters used: claim status.
     *
     * @param pageable Pageable params
     * @param status Claim status
     * @param include Include status
     * @return {@link Page} with queered {@link Claim} entities
     */
    Page<Claim> getCurrentClaimsPage(@NonNull Pageable pageable,
                                     @Nullable ClaimStatus status,
                                     @NonNull Boolean include);

    /**
     * Returns a {@link Claim} entity by ID.
     *
     * @param id Claim ID
     * @return {@link Claim} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws ActionForbiddenException Not enough permissions
     */
    Claim getClaim(@NonNull Long id) throws EntityNotFoundException, ActionForbiddenException;

    /**
     * Creates new {@link Claim} entity.
     *
     * @param request Claim params
     * @return Created {@link Claim} entity
     * @throws BadInputDataException Bad input data
     * @throws ActionInapplicableException One of the fireplaces is reserved
     */
    Claim createClaim(@NonNull ClaimRequest request)
            throws BadInputDataException, ActionInapplicableException;

    /**
     * Report {@link Claim}.
     *
     * @param id Claim ID
     * @param message Claim report message
     * @return Created {@link ClaimReport} entity
     * @throws EntityNotFoundException Claim not found
     */
    ClaimReport reportClaim(@NonNull Long id, @Nullable ClaimReportRequest message)
            throws EntityNotFoundException;

    /**
     * Updates {@link Claim} status to APPROVED and assigns fireplaces.
     *
     * @param id Claim ID
     * @param departureId Departure Fireplace ID
     * @param arrivalId Arrival Fireplace ID
     * @return Updated {@link Claim} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws ActionInapplicableException Cannot change claim status
     */
    Claim approveClaim(@NonNull Long id,
                       @NonNull Long departureId,
                       @NonNull Long arrivalId)
            throws EntityNotFoundException, ActionInapplicableException;

    /**
     * Updates {@link Claim} status to REJECTED or CANCELLED by ID.
     *
     * @param id Claim ID
     * @return Updated {@link Claim} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws ActionInapplicableException Cannot change claim status
     * @throws ActionForbiddenException When try to change not your own claim status
     */
    Claim cancelRejectClaim(@NonNull Long id)
            throws EntityNotFoundException, ActionInapplicableException, ActionForbiddenException;

    /**
     * Updates {@link Claim} status to COMPLETED.
     *
     * @param id Claim ID
     * @return Updated {@link Claim} entity
     * @throws EntityNotFoundException Not Found Exception
     * @throws ActionInapplicableException Cannot change claim status
     * @throws ActionForbiddenException When try to change not your own claim status
     */
    Claim completeClaim(@NonNull Long id)
            throws EntityNotFoundException, ActionForbiddenException, ActionInapplicableException;
}
