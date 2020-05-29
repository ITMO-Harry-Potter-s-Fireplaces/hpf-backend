package ru.fireplaces.harrypotter.itmo.fireplace.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.fireplace.service.ClaimService;

/**
 * Implementation of {@link ClaimService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = ClaimServiceImpl.SERVICE_VALUE)
public class ClaimServiceImpl implements ClaimService {

    public static final String SERVICE_VALUE = "ClaimServiceImpl";
}
