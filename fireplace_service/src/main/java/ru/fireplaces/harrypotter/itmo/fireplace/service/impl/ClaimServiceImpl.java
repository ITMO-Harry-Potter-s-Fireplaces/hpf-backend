package ru.fireplaces.harrypotter.itmo.fireplace.service.impl;

import org.slf4j.MDC;
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
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.response.User;
import ru.fireplaces.harrypotter.itmo.fireplace.feign.SecurityApiClient;
import ru.fireplaces.harrypotter.itmo.fireplace.service.ClaimService;
import ru.fireplaces.harrypotter.itmo.fireplace.service.FireplaceService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionInapplicableException;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

import java.util.List;

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
    private final SecurityApiClient securityApiClient;
    private final FireplaceService fireplaceService;

    @Autowired
    public ClaimServiceImpl(ClaimRepository claimRepository,
                            SecurityApiClient securityApiClient,
                            FireplaceService fireplaceService) {
        this.claimRepository = claimRepository;
        this.securityApiClient = securityApiClient;
        this.fireplaceService = fireplaceService;
    }

    @Override
    public Page<Claim> getClaimsPage(@NonNull Pageable pageable,
                                     @Nullable ClaimStatus status,
                                     @Nullable Long userId) throws EntityNotFoundException {
        if (userId == null) {
            if (status == null) {
                return claimRepository.findAll(pageable).map(claim -> {
                    claim.setUser(securityApiClient.getUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN),
                            claim.getUserId()).getMessage());
                    return claim;
                });
            }
            else {
                return claimRepository.findAllByStatus(pageable, status).map(claim -> {
                    claim.setUser(securityApiClient.getUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN),
                            claim.getUserId()).getMessage());
                    return claim;
                });
            }
        }
        else {
            User user = securityApiClient.getUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN), userId).getMessage();
            if (status == null) {
                return claimRepository.findAllByUserId(pageable, userId).map(claim -> {
                    claim.setUser(user);
                    return claim;
                });
            }
            else {
                return claimRepository.findAllByStatusAndUserId(pageable, status, userId).map(claim -> {
                    claim.setUser(user);
                    return claim;
                });
            }
        }
    }

    @Override
    public Page<Claim> getCurrentClaimsPage(@NonNull Pageable pageable,
                                            @Nullable ClaimStatus status) {
        User currentUser = securityApiClient.getCurrentUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN)).getMessage();
        if (status == null) {
            return claimRepository.findAllByUserId(pageable, currentUser.getId());
        }
        return claimRepository.findAllByStatusAndUserId(pageable, status, currentUser.getId());
    }

    @Override
    public Claim getClaim(@NonNull Long id) throws EntityNotFoundException {
        return claimRepository.findById(id).map(claim -> {
            claim.setUser(securityApiClient.getUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN),
                    claim.getUserId()).getMessage());
            return claim;
        }).orElseThrow(() -> new EntityNotFoundException(Claim.class, id));
    }

    private Claim getClaimNoUser(@NonNull Long id) throws EntityNotFoundException {
        return claimRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Claim.class, id));
    }

    @Override
    public Claim createClaim(@NonNull ClaimRequest request)
            throws BadInputDataException, ActionInapplicableException {
        List<String> blankFields = request.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(ClaimRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        Fireplace departureFp = fireplaceService.getFireplace(request.getDepartureId());
        Fireplace arrivalFp = fireplaceService.getFireplace(request.getArrivalId());
        if (departureFp.equals(arrivalFp)) {
            throw new ActionInapplicableException("Cannot use same fireplaces as departure and destination points");
        }
        else if (claimRepository.existsByDepartureTimeAndDeparture(request.getDepartureTime(), departureFp)
                || claimRepository.existsByDepartureTimeAndArrival(request.getDepartureTime(), departureFp)) {
            throw new ActionInapplicableException("Firaplace with ID " + request.getDepartureId() + "is already reserved.");
        }
        else if (claimRepository.existsByDepartureTimeAndDeparture(request.getDepartureTime(), arrivalFp)
                || claimRepository.existsByDepartureTimeAndArrival(request.getDepartureTime(), arrivalFp)) {
            throw new ActionInapplicableException("Firaplace with ID " + request.getArrivalId() + "is already reserved.");
        }
        request.setDeparture(departureFp);
        request.setArrival(arrivalFp);
        User currentUser = securityApiClient.getCurrentUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN)).getMessage();
        Claim claim = new Claim();
        claim.copy(request);
        claim.setUserId(currentUser.getId());
        return claimRepository.save(claim);
    }

    @Override
    public Claim approveClaim(@NonNull Long id, @NonNull Boolean approve)
            throws EntityNotFoundException, ActionInapplicableException {
        Claim claim = getClaimNoUser(id);
        if (claim.getStatus().equals(ClaimStatus.CREATED)) {
            claim.setStatus(approve ? ClaimStatus.APPROVED : ClaimStatus.REJECTED);
            return claimRepository.save(claim);
        }
        throw new ActionInapplicableException("Cannot change status of claim with status " + claim.getStatus());
    }

    @Override
    public Claim completeClaim(@NonNull Long id, @NonNull Boolean cancel)
            throws EntityNotFoundException, ActionInapplicableException, ActionForbiddenException {
        User currentUser = securityApiClient.getCurrentUser(MDC.get(Constants.KEY_MDC_AUTH_TOKEN)).getMessage();
        Claim claim = getClaimNoUser(id);
        if (!claim.getUserId().equals(currentUser.getId())) {
            throw new ActionForbiddenException("Cannot complete not your own claim");
        }
        if (cancel && claim.getStatus().equals(ClaimStatus.REJECTED)) {
            throw new ActionInapplicableException("Cannot cancel rejected claim");
        }
        else if (!cancel && !claim.getStatus().equals(ClaimStatus.APPROVED)) {
            throw new ActionInapplicableException("Cannot complete not approved claim");
        }
        claim.setStatus(cancel ? ClaimStatus.CANCELLED : ClaimStatus.COMPLETED);
        return claimRepository.save(claim);
    }
}
