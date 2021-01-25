package ru.fireplaces.harrypotter.itmo.ascpect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fireplaces.harrypotter.itmo.service.PermissionsService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;

import java.util.Arrays;

/**
 * Aspect to handle {@link AllowPermission} annotation.
 *
 * @author seniorkot
 */
@Aspect
@Component
public class SecurityAllowAspect {

    private final PermissionsService permissionsService;

    @Autowired
    public SecurityAllowAspect(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }


    @Pointcut("@annotation(allowPermission)")
    public void callAt(AllowPermission allowPermission) {

    }

    @Around(value = "callAt(allowPermission)", argNames = "jp,allowPermission")
    public Object around(ProceedingJoinPoint jp, AllowPermission allowPermission) throws Throwable {
        if (permissionsService.userHasRole(MDC.get(Constants.KEY_MDC_AUTH_TOKEN),
                Arrays.asList(allowPermission.roles()))) {
            return jp.proceed();
        }
        throw new ActionForbiddenException("Not enough permissions");
    }
}
