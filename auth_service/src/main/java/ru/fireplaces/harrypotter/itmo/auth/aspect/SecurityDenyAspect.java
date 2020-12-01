package ru.fireplaces.harrypotter.itmo.auth.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fireplaces.harrypotter.itmo.auth.service.PermissionsService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.DenyPermission;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;

import java.util.Arrays;

/**
 * Aspect to handle {@link DenyPermission} annotation.
 *
 * @author seniorkot
 */
@Aspect
@Component
public class SecurityDenyAspect {

    private final PermissionsService permissionsService;

    @Autowired
    public SecurityDenyAspect(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Pointcut("@annotation(denyPermission)")
    public void callAt(DenyPermission denyPermission) {

    }

    @Around(value = "callAt(denyPermission)", argNames = "jp,denyPermission")
    public Object around(ProceedingJoinPoint jp, DenyPermission denyPermission) throws Throwable {
        if (!permissionsService.userHasRole(MDC.get(Constants.KEY_MDC_AUTH_TOKEN),
                Arrays.asList(denyPermission.roles()))) {
            return jp.proceed();
        }
        throw new ActionForbiddenException("Not enough permissions");
    }
}
