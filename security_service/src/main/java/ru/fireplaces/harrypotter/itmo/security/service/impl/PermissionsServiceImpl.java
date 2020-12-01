package ru.fireplaces.harrypotter.itmo.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.AllowPermissionRequest;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.TokenVerificationRequest;
import ru.fireplaces.harrypotter.itmo.security.service.PermissionsService;
import ru.fireplaces.harrypotter.itmo.security.service.SecurityService;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;

import java.util.List;

/**
 * Implementation of {@link PermissionsService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = PermissionsServiceImpl.SERVICE_VALUE)
public class PermissionsServiceImpl implements PermissionsService {

    public static final String SERVICE_VALUE = "PermissionsServiceImpl";

    private final SecurityService securityService;

    @Autowired
    public PermissionsServiceImpl(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public boolean userHasRole(@NonNull String token,
                               @NonNull List<Role> roles) throws ActionForbiddenException {
        if (verifyToken(token)) {
            throw new ActionForbiddenException("Token has been expired");
        }
        return roles.contains(securityService.authorizeToken(token).getRole());
    }

    @Override
    public boolean userHasRole(@NonNull AllowPermissionRequest allowedRequest)
            throws ActionForbiddenException {
        return userHasRole(allowedRequest.getToken(), allowedRequest.getRoles());
    }

    @Override
    public boolean verifyToken(@NonNull String token) throws ActionForbiddenException {
        return securityService.validateTokenExpiration(token);
    }

    @Override
    public boolean verifyToken(@NonNull TokenVerificationRequest tokenRequest)
            throws ActionForbiddenException {
        return verifyToken(tokenRequest.getToken());
    }
}
