package ru.fireplaces.harrypotter.itmo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimReportRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.service.ClaimService;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.TokenVerification;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponseBuilder;
import ru.fireplaces.harrypotter.itmo.utils.response.PageResponse;

import java.net.URI;

/**
 * REST controller for claims.
 *
 * @author seniorkot
 */
@RestController
@RequestMapping(ClaimController.CONTROLLER_PATH)
public class ClaimController {

    /**
     * Controller path value.
     */
    public static final String CONTROLLER_PATH = "/claims";

    private static final Logger logger = LogManager.getLogger(ClaimController.class);

    private final ClaimService claimService;

    @Autowired
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    /**
     * Returns a page with list of {@link Claim} entities.<br>
     * Default page: 0; size: 20; sort: 'id'
     *
     * @param token Authorization token
     * @param pageable Pageable params
     * @param status Filter: claim status
     * @param include Filter: include status (bool)
     * @param userId Filter: user ID
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link org.springframework.data.domain.Page} with list of {@link Claim} objects
     */
    @AllowPermission(roles = {Role.ADMIN, Role.MODERATOR, Role.MINISTER})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<Claim> getClaims(@RequestHeader(value = "Authorization") String token,
                                         @PageableDefault(size = 20, sort = "id") Pageable pageable,
                                         @RequestParam(required = false) ClaimStatus status,
                                         @RequestParam(defaultValue = "true") Boolean include,
                                         @RequestParam(name = "user_id", required = false) Long userId) {
        logger.info("getClaims: pageable=" + pageable + "; status=" + status
                + "; userId=" + userId + "; token=" + token);
        PageResponse<Claim> response = CodeMessageResponseBuilder.page(
                claimService.getClaimsPage(pageable, status, include, userId));
        logger.info("getClaims: response=" + response.getBody());
        return response;
    }

    /**
     * Returns {@link Claim} entity by ID.
     *
     * @param token Authorization token
     * @param id Claim ID
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link Claim} object
     */
    @TokenVerification
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<Claim> getClaim(@RequestHeader(value = "Authorization") String token,
                                               @PathVariable Long id) {
        logger.info("getClaim: id=" + id + "; token=" + token);
        CodeMessageResponse<Claim> response = CodeMessageResponseBuilder.ok(claimService.getClaim(id));
        logger.info("getClaim: response=" + response.getBody());
        return response;
    }

    /**
     * Returns a page with list of {@link Claim} entities related to current user.<br>
     * Default page: 0; size: 20; sort: 'id'
     *
     * @param token Authorization token
     * @param pageable Pageable params
     * @param status Filter: claim status
     * @param include Filter: include status (bool)
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link org.springframework.data.domain.Page} with list of {@link Claim} objects
     */
    @TokenVerification
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<Claim> getCurrentClaims(@RequestHeader(value = "Authorization") String token,
                                                @PageableDefault(size = 20, sort = "id") Pageable pageable,
                                                @RequestParam(required = false) ClaimStatus status,
                                                @RequestParam(defaultValue = "true") Boolean include) {
        logger.info("getCurrentClaims: pageable=" + pageable + "; status=" + status + "; token=" + token);
        PageResponse<Claim> response = CodeMessageResponseBuilder.page(
                claimService.getCurrentClaimsPage(pageable, status, include));
        logger.info("getCurrentClaims: response=" + response.getBody());
        return response;
    }

    /**
     * Creates new {@link Claim} entity.
     *
     * @param token Authorization token
     * @param claimRequest Claim params
     * @return <b>Response code</b>: 201
     */
    @TokenVerification
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<URI> createClaim(@RequestHeader(value = "Authorization") String token,
                                                @RequestBody ClaimRequest claimRequest,
                                                UriComponentsBuilder uriComponentsBuilder) {
        logger.info("createClaim: claimRequest=" + claimRequest + "; token=" + token);
        final long claimId = claimService.createClaim(claimRequest).getId();
        UriComponents uriComponents =
                uriComponentsBuilder.path(CONTROLLER_PATH + "/{id}").buildAndExpand(claimId);
        CodeMessageResponse<URI> response = CodeMessageResponseBuilder.created(uriComponents.toUri());
        logger.info("createClaim: response=" + response.getBody());
        return response;
    }

    /**
     * Reports a {@link Claim} by ID.
     *
     * @param token Authorization token
     * @param id Claim ID
     * @param message Report message
     * @return <b>Response code</b>: 204
     */
    @AllowPermission(roles = {Role.ADMIN, Role.MODERATOR, Role.MINISTER})
    @PostMapping(value = "/{id}/report", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> reportClaim(@RequestHeader(value = "Authorization") String token,
                                                   @PathVariable Long id,
                                                   @RequestBody(required = false) ClaimReportRequest message) {
        logger.info("reportClaim: id=" + id + "; message=" + message + "; token=" + token);
        claimService.reportClaim(id, message);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("reportClaim: response=" + response.getBody());
        return response;
    }

    /**
     * Changes {@link Claim} status to APPROVED and assigns fireplaces by ID.
     *
     * @param token Authorization token
     * @param id Claim ID
     * @param departureId Departure Fireplace ID
     * @param arrivalId Arrival Fireplace ID
     * @return <b>Response code</b>: 204
     */
    @AllowPermission(roles = {Role.ADMIN, Role.MODERATOR})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> approveClaim(@RequestHeader(value = "Authorization") String token,
                                                    @PathVariable Long id,
                                                    @RequestParam(name = "departure") Long departureId,
                                                    @RequestParam(name = "arrival") Long arrivalId) {
        logger.info("approveClaim: id=" + id + "; departureId=" + departureId
                + "; arrivalId=" + arrivalId + "; token=" + token);
        claimService.approveClaim(id, departureId, arrivalId);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("approveClaim: response=" + response.getBody());
        return response;
    }

    /**
     * Changes {@link Claim} status to CANCELLED or REJECTED by ID.
     *
     * @param token Authorization token
     * @param id Claim ID
     * @return <b>Response code</b>: 204
     */
    @TokenVerification
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> cancelRejectClaim(@RequestHeader(value = "Authorization") String token,
                                                         @PathVariable Long id) {
        logger.info("completeClaim: id=" + id + "; token=" + token);
        claimService.cancelRejectClaim(id);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("completeClaim: response=" + response.getBody());
        return response;
    }

    /**
     * Changes {@link Claim} status to COMPLETED by ID.
     *
     * @param token Authorization token
     * @param id Claim ID
     * @return <b>Response code</b>: 204
     */
    @TokenVerification
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> completeClaim(@RequestHeader(value = "Authorization") String token,
                                                     @PathVariable Long id) {
        logger.info("completeClaim: id=" + id + "; token=" + token);
        claimService.completeClaim(id);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("completeClaim: response=" + response.getBody());
        return response;
    }
}
