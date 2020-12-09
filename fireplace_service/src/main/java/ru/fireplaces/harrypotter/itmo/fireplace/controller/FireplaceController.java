package ru.fireplaces.harrypotter.itmo.fireplace.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.CoordsRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.service.FireplaceService;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.TokenVerification;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponseBuilder;
import ru.fireplaces.harrypotter.itmo.utils.response.PageResponse;

import java.net.URI;

/**
 * REST controller for fireplaces.
 *
 * @author seniorkot
 */
@RestController
@RequestMapping(FireplaceController.CONTROLLER_PATH)
public class FireplaceController {

    /**
     * Controller path value.
     */
    public static final String CONTROLLER_PATH = "/fireplaces";

    private static final Logger logger = LogManager.getLogger(FireplaceController.class);

    private final FireplaceService fireplaceService;

    @Autowired
    public FireplaceController(FireplaceService fireplaceService) {
        this.fireplaceService = fireplaceService;
    }

    /**
     * Returns a page with list of {@link Fireplace} entities.<br>
     * Default page: 0; size: 20; sort: 'id'<br>
     * With no fireplaceParams returns a page of first entities.
     *
     * @param token Authorization token
     * @param pageable Pageable params
     * @param lat Latitude (required if lng is not null)
     * @param lng Longitude (required if lat is not null)
     * @param radius Search radius (required if lat and lng are not null)
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link org.springframework.data.domain.Page} with list of {@link Fireplace} objects
     */
    @TokenVerification
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<Fireplace> getFireplaces(@RequestHeader(value = "Authorization") String token,
                                                 @PageableDefault(size = 20, sort = "id") Pageable pageable,
                                                 @RequestParam(required = false) Float lat,
                                                 @RequestParam(required = false) Float lng,
                                                 @RequestParam(required = false) Double radius) {
        logger.info("getFireplaces: pageable=" + pageable + "; coords=(" + lat + ", " + lng
                + "; radius=" + radius + "); token=" + token);
        PageResponse<Fireplace> response = CodeMessageResponseBuilder.page(
                fireplaceService.getFireplacesPage(pageable, new CoordsRequest(lat, lng, radius)));
        logger.info("getFireplaces: response=" + response.getBody());
        return response;
    }

    /**
     * Returns {@link Fireplace} entity by ID.
     *
     * @param token Authorization token
     * @param id Fireplace ID
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link Fireplace} object
     */
    @TokenVerification
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<Fireplace> getFireplace(@RequestHeader(value = "Authorization") String token,
                                                       @PathVariable Long id) {
        logger.info("getFireplace: id=" + id + "; token=" + token);
        CodeMessageResponse<Fireplace> response =
                CodeMessageResponseBuilder.ok(fireplaceService.getFireplace(id));
        logger.info("getFireplace: response=" + response.getBody());
        return response;
    }

    /**
     * Creates new {@link Fireplace} entity.
     *
     * @param token Authorization token
     * @param fireplaceRequest Fireplace params
     * @return @return <b>Response code</b>: 201
     */
    @AllowPermission(roles = {Role.USER})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<URI> createFireplace(@RequestHeader(value = "Authorization") String token,
                                                    @RequestBody FireplaceRequest fireplaceRequest,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        logger.info("createFireplace: fireplaceRequest=" + fireplaceRequest + "; token=" + token);
        final long fireplaceId = fireplaceService.createFireplace(fireplaceRequest).getId();
        UriComponents uriComponents =
                uriComponentsBuilder.path(CONTROLLER_PATH + "/{id}").buildAndExpand(fireplaceId);
        CodeMessageResponse<URI> response = CodeMessageResponseBuilder.created(uriComponents.toUri());
        logger.info("createFireplace: response=" + response.getBody());
        return response;
    }

    /**
     * Updates {@link Fireplace} entity by ID.
     *
     * @param token Authorization token
     * @param id Fireplace ID
     * @param fireplaceRequest Fireplace params
     * @param copy Copy or update params
     * @return <b>Response code</b>: 204
     */
    @AllowPermission(roles = {Role.USER})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> updateFireplace(@RequestHeader(value = "Authorization") String token,
                                                       @PathVariable Long id,
                                                       @RequestBody FireplaceRequest fireplaceRequest,
                                                       @RequestParam(defaultValue = "true") Boolean copy) {
        logger.info("updateFireplace: id=" + id + "; fireplaceRequest=" + fireplaceRequest
                + "; copy=" + copy + "; token=" + token);
        fireplaceService.updateFireplace(id, fireplaceRequest, copy);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("updateFireplace: response=" + response.getBody());
        return response;
    }

    /**
     * Deletes {@link Fireplace} entity by ID.
     *
     * @param token Authorization token
     * @param id Fireplace ID
     * @return <b>Response code</b>: 204
     */
    @TokenVerification
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> deleteFireplace(@RequestHeader(value = "Authorization") String token,
                                                       @PathVariable Long id) {
        logger.info("deleteFireplace: id=" + id + "; token=" + token);
        fireplaceService.deleteFireplace(id);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("deleteFireplace: response=" + response.getBody());
        return response;
    }
}
