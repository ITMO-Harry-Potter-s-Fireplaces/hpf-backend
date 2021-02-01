package ru.fireplaces.harrypotter.itmo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.fireplaces.harrypotter.itmo.domain.model.AuthLog;
import ru.fireplaces.harrypotter.itmo.domain.model.User;
import ru.fireplaces.harrypotter.itmo.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.response.LoginResponse;
import ru.fireplaces.harrypotter.itmo.service.AuthService;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.TokenVerification;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponseBuilder;
import ru.fireplaces.harrypotter.itmo.utils.response.PageResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for register and login endpoints.
 *
 * @author seniorkot
 */
@RestController
@RequestMapping(AuthController.CONTROLLER_PATH)
public class AuthController {

    /**
     * Controller path value.
     */
    public static final String CONTROLLER_PATH = "/auth";

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers new user in the system and authenticates him.
     *
     * @param userRequest Login, password and user data
     * @param request Request object
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link LoginResponse} with user ID and JSON Web Token value
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<LoginResponse> register(@RequestBody UserRequest userRequest,
                                                       HttpServletRequest request) {
        logger.info("register: userRequest=" + userRequest);
        CodeMessageResponse<LoginResponse> response =
                CodeMessageResponseBuilder.ok(authService.register(userRequest, request));
        logger.info("register: response=" + response.getBody());
        return response;
    }

    /**
     * Authenticates user in the system.
     *
     * @param loginRequest Login and password
     * @param request Request object
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link LoginResponse} with user ID and JSON Web Token value
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest,
                                                    HttpServletRequest request) {
        logger.info("login: loginRequest=" + loginRequest);
        CodeMessageResponse<LoginResponse> response =
                CodeMessageResponseBuilder.ok(authService.login(loginRequest, request));
        logger.info("login: response=" + response.getBody());
        return response;
    }

    @AllowPermission(roles = {Role.ADMIN, Role.MODERATOR})
    @GetMapping(value = "/history/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<AuthLog> getHistory(@RequestHeader(value = "Authorization") String token,
                                            @PageableDefault(size = 20, sort = "id") Pageable pageable,
                                            @PathVariable Long id) {
        logger.info("getHistory: pageable=" + pageable + "; userId=" + id + "; token=" + token);
        PageResponse<AuthLog> response =
                CodeMessageResponseBuilder.page(authService.getAuthHistory(pageable, id));
        logger.info("getHistory: response=" + response.getBody());
        return response;
    }

    @TokenVerification
    @GetMapping(value = "/history",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<AuthLog> getCurrentHistory(@RequestHeader(value = "Authorization") String token,
                                                   @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        logger.info("getCurrentHistory: pageable=" + pageable + "; token=" + token);
        PageResponse<AuthLog> response =
                CodeMessageResponseBuilder.page(authService.getCurrentAuthHistory(pageable));
        logger.info("getCurrentHistory: response=" + response.getBody());
        return response;
    }
}
