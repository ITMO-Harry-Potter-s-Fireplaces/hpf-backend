package ru.fireplaces.harrypotter.itmo.auth.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.response.LoginResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;

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

    @Autowired
    public AuthController() {
        // TODO: Init auth service
    }

    /**
     * Registers new user in the system and authenticates him.
     *
     * @param request Login and password
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link LoginResponse} with user ID and JSON Web Token value
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<LoginResponse> register(@RequestBody LoginRequest request) {
        logger.info("register: loginRequest=" + request);
        CodeMessageResponse<LoginResponse> response = null; // TODO: Fill response
        logger.info("register: response=" + response.getBody());
        return response;
    }

    /**
     * Authenticates user in the system.
     *
     * @param request Login and password
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link LoginResponse} with user ID and JSON Web Token value
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("login: loginRequest=" + request);
        CodeMessageResponse<LoginResponse> response = null; // TODO: Fill response
        logger.info("login: response=" + response.getBody());
        return response;
    }
}
