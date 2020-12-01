package ru.fireplaces.harrypotter.itmo.security.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fireplaces.harrypotter.itmo.security.service.PermissionsService;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.UserRoleRequest;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.TokenVerificationRequest;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponseBuilder;

/**
 * REST controller with endpoints to verify
 * {@link ru.fireplaces.harrypotter.itmo.security.domain.model.User}
 * permissions via JSON Web Token.
 *
 * @author seniorkot
 */
@RestController
@RequestMapping(PermissionsController.CONTROLLER_PATH)
public class PermissionsController {

    /**
     * Controller path value.
     */
    public static final String CONTROLLER_PATH = "/permissions";

    private static final Logger logger = LogManager.getLogger(PermissionsController.class);

    private final PermissionsService permissionsService;

    @Autowired
    public PermissionsController(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    /**
     * Checks user permissions with JSON Web Token and user
     * {@link ru.fireplaces.harrypotter.itmo.utils.enums.Role}.
     *
     * @param request JWT and action names
     * @return <b>Response code</b>: 204 - user has required permissions,
     * 403 - not enough permissions or token is invalid
     */
    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> checkUserRole(@RequestBody UserRoleRequest request) {
        logger.info("checkUserRole: request=" + request);
        CodeMessageResponse<String> response;
        if (permissionsService.userHasRole(request)) {
            response = CodeMessageResponseBuilder.noContent();
        }
        else {
            response = CodeMessageResponseBuilder.forbidden("Not enough permissions");
        }
        logger.info("checkUserRole: response=" + response.getBody());
        return response;
    }

    /**
     * Checks if Auth token hasn't been expired.
     *
     * @param request JSON Web Token
     * @return <b>Response code</b>: 204 - token is valid,
     * 403 - token has been expired or it's invalid
     */
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> verifyToken(@RequestBody TokenVerificationRequest request) {
        logger.info("verifyToken: request=" + request);
        CodeMessageResponse<String> response;
        if (permissionsService.verifyToken(request)) {
            response = CodeMessageResponseBuilder.noContent();
        }
        else {
            response = CodeMessageResponseBuilder.forbidden("Auth token has been expired");
        }
        logger.info("verifyToken: response=" + response.getBody());
        return response;
    }

}
