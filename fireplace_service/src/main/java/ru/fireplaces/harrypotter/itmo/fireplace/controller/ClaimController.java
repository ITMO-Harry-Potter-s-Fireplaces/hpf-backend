package ru.fireplaces.harrypotter.itmo.fireplace.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
