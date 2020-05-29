package ru.fireplaces.harrypotter.itmo.fireplace.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public static final String CONTROLLER_PATH = "";

    private static final Logger logger = LogManager.getLogger(FireplaceController.class);
}
