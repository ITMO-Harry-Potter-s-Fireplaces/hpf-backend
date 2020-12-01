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
import ru.fireplaces.harrypotter.itmo.auth.service.UserService;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.TokenVerification;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns a page with list of {@link User} entities.<br>
     * Default page: 0; size: 20; sort: 'id'<br>
     * Default roleIds: null (result equals to "")
     *
     * @param token Authorization token
     * @param pageable Pageable params
     * @param roleIds Filter: role IDs
     * @param active Account status
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link org.springframework.data.domain.Page} with list of {@link User} objects
     */
    @AllowPermission(roles = {Role.ADMIN, Role.MODERATOR})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<User> getAllUsers(@RequestHeader(value = "Authorization") String token,
                                          @PageableDefault(size = 20, sort = "id") Pageable pageable,
                                          @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                                          @RequestParam(value = "active", defaultValue = "true") Boolean active) {
        logger.info("getAllUsers: pageable=" + pageable + "; roleIds=" + roleIds
                + "; active=" + active + "; token=" + token);
        PageResponse<User> response =
                CodeMessageResponseBuilder.page(userService.getUsersPage(pageable, roleIds, active));
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
    @TokenVerification
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
     * Returns current {@link User} entity.
     *
     * @param token Authorization token
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link User} object
     */
    @TokenVerification
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<User> getCurrentUser(@RequestHeader(value = "Authorization") String token) {
        logger.info("getCurrentUser: token=" + token);
        CodeMessageResponse<User> response =
                CodeMessageResponseBuilder.ok(userService.getCurrentUser());
        logger.info("getCurrentUser: response=" + response.getBody());
        return response;
    }

    /**
     * Updates current {@link User} entity.
     *
     * @param token Authorization token
     * @param user User params
     * @return <b>Response code</b>: 204
     */
    @TokenVerification
    @PutMapping(value = "/current", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> updateCurrentUser(@RequestHeader(value = "Authorization") String token,
                                                         @RequestBody UserRequest user,
                                                         @RequestParam(defaultValue = "true") Boolean copy) {
        logger.info("updateCurrentUser: userRequest=" + user + "; token=" + token);
        userService.updateCurrentUser(user, copy);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("updateUser: response=" + response.getBody());
        return response;
    }

    /**
     * Deletes current {@link User} entity.
     *
     * @param token Authorization token
     * @return <b>Response code</b>: 204
     */
    @TokenVerification
    @DeleteMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> deleteCurrentUser(@RequestHeader(value = "Authorization") String token) {
        logger.info("deleteCurrentUser: token=" + token);
        userService.deleteCurrentUser();
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("deleteCurrentUser: response=" + response.getBody());
        return response;
    }

    /**
     * Updates {@link User} entity {@link Role} by ID.
     *
     * @param token Authorization token
     * @param id User ID
     * @param role User {@link Role}
     * @return <b>Response code</b>: 204
     */
    @AllowPermission(roles = {Role.ADMIN})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> updateUserRole(@RequestHeader(value = "Authorization") String token,
                                                      @PathVariable Long id,
                                                      @RequestParam(name = "role") Role role) {
        logger.info("updateUserRole: id=" + id + "role=" + role + "; token=" + token);
        userService.changeUserRole(id, role);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("updateUserRole: response=" + response.getBody());
        return response;
    }

    /**
     * Deletes {@link User} entity by ID.
     *
     * @param token Authorization token
     * @return <b>Response code</b>: 204
     */
    @AllowPermission(roles = {Role.ADMIN})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> deleteUser(@RequestHeader(value = "Authorization") String token,
                                                  @PathVariable Long id) {
        logger.info("deleteUser: id=" + id + "; token=" + token);
        userService.deleteUser(id);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("deleteUser: response=" + response.getBody());
        return response;
    }
}
