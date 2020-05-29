package ru.fireplaces.harrypotter.itmo.fireplace.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.service.FireplaceService;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponseBuilder;
import ru.fireplaces.harrypotter.itmo.utils.response.PageResponse;

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
     * @param fireplaceParams Filter: latitude and longitude
     * @return <b>Response code</b>: 200<br>
     *     <b>Body</b>: {@link org.springframework.data.domain.Page} with list of {@link Fireplace} objects
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<Fireplace> getFireplaces(@RequestHeader(value = "Authorization") String token,
                                                 @PageableDefault(size = 20, sort = "id") Pageable pageable,
                                                 @RequestBody(required = false) FireplaceRequest fireplaceParams) {
        logger.info("getFireplaces: pageable=" + pageable + "; params=" + fireplaceParams + "; token=" + token);
        PageResponse<Fireplace> response =
                CodeMessageResponseBuilder.page(fireplaceService.getFireplacesPage(pageable, fireplaceParams));
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
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<Fireplace> getFireplace(@RequestHeader(value = "Authorization") String token,
                                                       @PathVariable Long id) {
        logger.info("getFireplace: id=" + id + "; token=" + token);
        CodeMessageResponse<Fireplace> response =
                CodeMessageResponseBuilder.ok(fireplaceService.getFireplace(id));
        logger.info("getFireplace: response=" + response.getBody());
        return response;
    }
}
