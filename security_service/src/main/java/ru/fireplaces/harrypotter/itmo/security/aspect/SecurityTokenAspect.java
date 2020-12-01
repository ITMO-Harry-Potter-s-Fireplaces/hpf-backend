package ru.fireplaces.harrypotter.itmo.security.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.fireplaces.harrypotter.itmo.security.service.PermissionsService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.TokenVerification;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;

/**
 * Aspect to handle {@link TokenVerification} annotation.
 *
 * @author seniorkot
 */
@Aspect
@Component
public class SecurityTokenAspect {

    private final PermissionsService permissionsService;

    @Autowired
    public SecurityTokenAspect(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Pointcut("@annotation(tokenVerification)")
    public void callAt(TokenVerification tokenVerification) {

    }

    @Around(value = "callAt(tokenVerification)", argNames = "jp,tokenVerification")
    public Object around(ProceedingJoinPoint jp, TokenVerification tokenVerification) throws Throwable {
        if (permissionsService.verifyToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN))) {
            return jp.proceed();
        }
        throw new ActionForbiddenException("Not enough permissions");
    }
}
