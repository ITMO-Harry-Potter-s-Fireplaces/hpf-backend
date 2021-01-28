package ru.fireplaces.harrypotter.itmo.service.impl;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.dao.ClaimLogRepository;
import ru.fireplaces.harrypotter.itmo.domain.dao.ClaimReportRepository;
import ru.fireplaces.harrypotter.itmo.domain.dao.ClaimRepository;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.domain.model.*;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimReportRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.service.ClaimService;
import ru.fireplaces.harrypotter.itmo.service.FireplaceService;
import ru.fireplaces.harrypotter.itmo.service.SecurityService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.exception.*;

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
    private final ClaimReportRepository claimReportRepository;
    private final ClaimLogRepository claimLogRepository;
    private final SecurityService securityService;
    private final FireplaceService fireplaceService;

    @Autowired
    public ClaimServiceImpl(ClaimRepository claimRepository,
                            ClaimReportRepository claimReportRepository,
                            ClaimLogRepository claimLogRepository,
                            SecurityService securityService,
                            FireplaceService fireplaceService) {
        this.claimRepository = claimRepository;
        this.claimReportRepository = claimReportRepository;
        this.claimLogRepository = claimLogRepository;
        this.securityService = securityService;
        this.fireplaceService = fireplaceService;
    }

    @Override
    public Page<Claim> getClaimsPage(@NonNull Pageable pageable,
                                     @Nullable ClaimStatus status,
                                     @NonNull Boolean include,
                                     @Nullable Long userId) throws EntityNotFoundException {
        Page<Claim> claimsPage;
        if (userId == null) {
            if (status == null) {
                claimsPage = claimRepository.findAll(pageable);
            }
            else {
                claimsPage = include ? claimRepository.findAllByStatus(pageable, status)
                        : claimRepository.findAllByStatusNot(pageable, status);
            }
        }
        else {
            if (status == null) {
                claimsPage = claimRepository.findAllByUserId(pageable, userId);
            }
            else {
                claimsPage = include ? claimRepository.findAllByStatusAndUserId(pageable, status, userId)
                        : claimRepository.findAllByStatusNotAndUserId(pageable, status, userId);
            }
        }
        claimsPage.map(claim -> {
            claim.setDeparture(new Coordinates(claim.getDepartureLat(), claim.getDepartureLng()));
            claim.setArrival(new Coordinates(claim.getArrivalLat(), claim.getArrivalLng()));
            return claim;
        });
        return claimsPage;
    }

    @Override
    public Page<Claim> getCurrentClaimsPage(@NonNull Pageable pageable,
                                            @Nullable ClaimStatus status,
                                            @NonNull Boolean include) {
        User currentUser = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        Page<Claim> claimsPage;
        if (status == null) {
            claimsPage = claimRepository.findAllByUserId(pageable, currentUser.getId());
        }
        else {
            claimsPage = include ? claimRepository.findAllByStatusAndUserId(pageable, status, currentUser.getId())
                    : claimRepository.findAllByStatusNotAndUserId(pageable, status, currentUser.getId());
        }
        claimsPage.map(claim -> {
            claim.setDeparture(new Coordinates(claim.getDepartureLat(), claim.getDepartureLng()));
            claim.setArrival(new Coordinates(claim.getArrivalLat(), claim.getArrivalLng()));
            return claim;
        });
        return claimsPage;
    }

    @Override
    public Claim getClaim(@NonNull Long id)
            throws EntityNotFoundException, ActionForbiddenException {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Claim.class, id));
        User currentUser = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        if (currentUser.getRole().equals(Role.USER) && !claim.getUser().equals(currentUser)) {
            throw new ActionForbiddenException("Not allowed to get other user's claim");
        }
        claim.setDeparture(new Coordinates(claim.getDepartureLat(), claim.getDepartureLng()));
        claim.setArrival(new Coordinates(claim.getArrivalLat(), claim.getArrivalLng()));
        return claim;
    }

    @Override
    public Page<ClaimLog> getClaimLogs(@NonNull Pageable pageable, @NonNull Long id)
            throws EntityNotFoundException, ActionForbiddenException {
        Claim claim = getClaim(id);
        User currentUser = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        if (currentUser.getRole().equals(Role.USER) && !claim.getUser().equals(currentUser)) {
            throw new ActionForbiddenException("Not allowed to get other user's claim");
        }
        return claimLogRepository.findAllByClaim(pageable, claim);
    }

    @Override
    public Claim createClaim(@NonNull ClaimRequest request)
            throws BadInputDataException, ActionInapplicableException {
        List<String> blankFields = request.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(ClaimRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        User currentUser = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        Claim claim = new Claim();
        claim.copy(request);
        claim.setUser(currentUser);
        Claim savedClaim = claimRepository.save(claim);
        ClaimLog claimLog = new ClaimLog(savedClaim, currentUser, null, ClaimStatus.CREATED);
        claimLogRepository.save(claimLog);
        return savedClaim;
    }

    @Override
    public ClaimReport reportClaim(@NonNull Long id, @Nullable ClaimReportRequest message)
            throws EntityNotFoundException {
        Claim claim = getClaim(id);
        User reporter = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        if (claimReportRepository.existsByClaimAndReporter(claim, reporter)) {
            throw new ActionInapplicableException("You have already reported claim with ID " + id);
        }
        ClaimReport report = new ClaimReport();
        report.setClaim(claim);
        report.setReporter(reporter);
        if (message != null) {
            report.setMessage(message.getMessage());
        }
        claim.setReportsCount(claim.getReportsCount() + 1L);
        return claimReportRepository.save(report);
    }

    @Override
    public Claim approveClaim(@NonNull Long id,
                              @NonNull Long departureId,
                              @NonNull Long arrivalId) throws EntityNotFoundException, ActionInapplicableException {
        User currentUser = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        Claim claim = getClaim(id);
        if (claim.getStatus().equals(ClaimStatus.CREATED)) {
            Fireplace departure = fireplaceService.getFireplace(departureId);
            Fireplace arrival = fireplaceService.getFireplace(arrivalId);
            claim.setDepartureFireplace(departure);
            claim.setArrivalFireplace(arrival);
            claim.setStatus(ClaimStatus.APPROVED);
            ClaimLog claimLog = new ClaimLog(claim, currentUser, ClaimStatus.CREATED, ClaimStatus.APPROVED);
            claimLogRepository.save(claimLog);
            return claimRepository.save(claim);
        }
        throw new ActionInapplicableException("Cannot approve claim with status " + claim.getStatus());
    }

    @Override
    public Claim cancelRejectClaim(@NonNull Long id)
            throws EntityNotFoundException, ActionInapplicableException, ActionForbiddenException {
        User currentUser = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        Claim claim = getClaim(id);
        if (claim.getUser().equals(currentUser)) {
            if (claim.getStatus().equals(ClaimStatus.CREATED) || claim.getStatus().equals(ClaimStatus.APPROVED)) {
                ClaimLog claimLog = new ClaimLog(claim, currentUser, claim.getStatus(), ClaimStatus.CANCELLED);
                claim.setStatus(ClaimStatus.CANCELLED);
                claimLogRepository.save(claimLog);
            }
            else {
                throw new ActionInapplicableException("Cannot cancel claim with status " + claim.getStatus());
            }
        }
        else if (currentUser.getRole().equals(Role.ADMIN) || currentUser.getRole().equals(Role.MODERATOR)) {
            if (claim.getStatus().equals(ClaimStatus.CREATED)) {
                ClaimLog claimLog = new ClaimLog(claim, currentUser, ClaimStatus.CREATED, ClaimStatus.REJECTED);
                claim.setStatus(ClaimStatus.REJECTED);
                claimLogRepository.save(claimLog);
            }
            else {
                throw new ActionInapplicableException("Cannot reject claim with status " + claim.getStatus());
            }
        }
        else {
            throw new ActionForbiddenException("Cannot proceed not your own claim");
        }
        return claimRepository.save(claim);
    }

    @Override
    public Claim completeClaim(@NonNull Long id)
            throws EntityNotFoundException, ActionForbiddenException, ActionInapplicableException {
        User currentUser = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        Claim claim = getClaim(id);
        if (claim.getUser().equals(currentUser)) {
            if (claim.getStatus().equals(ClaimStatus.APPROVED)) {
                ClaimLog claimLog = new ClaimLog(claim, currentUser, ClaimStatus.APPROVED, ClaimStatus.COMPLETED);
                claim.setStatus(ClaimStatus.COMPLETED);
                claimLogRepository.save(claimLog);
                return claimRepository.save(claim);
            }
            throw new ActionInapplicableException("Cannot complete claim with status " + claim.getStatus());
        }
        throw new ActionForbiddenException("Cannot complete not your own claim");
    }
}
