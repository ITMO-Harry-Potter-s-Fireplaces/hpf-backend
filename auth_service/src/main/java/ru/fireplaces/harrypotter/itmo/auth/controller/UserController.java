package ru.fireplaces.harrypotter.itmo.auth.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.auth.service.SecurityService;
import ru.fireplaces.harrypotter.itmo.auth.service.UserService;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponseBuilder;
import ru.fireplaces.harrypotter.itmo.utils.response.PageResponse;

import java.util.List;

/**
 * REST controller for users.
 *
 * @author seniorkot
 */
@RestController
@RequestMapping(UserController.CONTROLLER_PATH)
public class UserController {

    /**
     * Controller path value.
     */
    public static final String CONTROLLER_PATH = "/users";

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public UserController(UserService userService,
                          SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * Returns a page with list of {@link User} entities.<br>
     * Default page: 0; size: 20; sort: 'id'<br>
     * Default roleIds: null (result equals to "")
     *
     * @param token Authorization token
     * @param pageable Pageable params
     * @param roleIds Filter: role IDs
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link org.springframework.data.domain.Page} with list of {@link User} objects
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<User> getAllUsers(@RequestHeader(value = "Authorization") String token,
                                          @PageableDefault(size = 20, sort = "id") Pageable pageable,
                                          @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        logger.info("getAllUsers: pageable=" + pageable + "; roleIds=" + roleIds + "; token=" + token);
        PageResponse<User> response =
                CodeMessageResponseBuilder.page(userService.getUsersPage(pageable, roleIds));
        logger.info("getAllUsers: response=" + response.getBody());
        return response;
    }

    /**
     * Returns {@link User} entity by ID.
     *
     * @param token Authorization token
     * @param id User ID
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link User} object
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<User> getUser(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable Long id) {
        logger.info("getUser: id=" + id + "; token=" + token);
        CodeMessageResponse<User> response =
                CodeMessageResponseBuilder.ok(userService.getUser(id));
        logger.info("getUser: response=" + response.getBody());
        return response;
    }

    /**
     * Returns {@link User} entity of current user.
     *
     * @param token Authorization token
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link User} object
     */
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<User> getCurrentUser(@RequestHeader(value = "Authorization") String token) {
        logger.info("getCurrentUser: token=" + token);
        CodeMessageResponse<User> response =
                CodeMessageResponseBuilder.ok(securityService.authorizeToken(token));
        logger.info("getCurrentUser: response=" + response.getBody());
        return response;
    }

    /**
     * Updates current user {@link User} entity by ID.
     *
     * @param token Authorization token
     * @param user User params
     * @return <b>Response code</b>: 204
     */
    @PutMapping(value = "/current", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> updateCurrentUser(@RequestHeader(value = "Authorization") String token,
                                                         @RequestBody UserRequest user) {
        logger.info("updateCurrentUser: userRequest=" + user + "; token=" + token);
        userService.updateUser(securityService.authorizeToken(token).getId(), user);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("updateUser: response=" + response.getBody());
        return response;
    }
}
