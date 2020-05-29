package ru.fireplaces.harrypotter.itmo.fireplace.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.fireplace.service.FireplaceService;

/**
 * Implementation of {@link FireplaceService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = FireplaceServiceImpl.SERVICE_VALUE)
public class FireplaceServiceImpl implements FireplaceService {

    public static final String SERVICE_VALUE = "FireplaceServiceImpl";

}
