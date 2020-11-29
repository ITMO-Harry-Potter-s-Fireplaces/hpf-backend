package ru.fireplaces.harrypotter.itmo.auth.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;

/**
 * Aspect to handle {@link AllowPermission} annotation.
 *
 * @author seniorkot
 */
@Aspect
@Component
public class SecurityAllowAspect {

    @Pointcut("@annotation(allowPermission)")
    public void callAt(AllowPermission allowPermission) {

    }

    @Around(value = "callAt(allowPermission)", argNames = "jp,allowPermission")
    public Object around(ProceedingJoinPoint jp, AllowPermission allowPermission) throws Throwable {

        throw new ActionForbiddenException("Not enough permissions");
    }
}
