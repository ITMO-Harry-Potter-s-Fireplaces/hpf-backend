package ru.fireplaces.harrypotter.itmo.fireplace.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.fireplaces.harrypotter.itmo.fireplace.feign.SecurityApiClient;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.UserRoleRequest;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessage;

import java.util.Arrays;

/**
 * Aspect to handle {@link AllowPermission} annotation.
 *
 * @author seniorkot
 */
@Aspect
@Component
public class SecurityAllowAspect {

    private final SecurityApiClient securityApiClient;

    @Autowired
    public SecurityAllowAspect(SecurityApiClient securityApiClient) {
        this.securityApiClient = securityApiClient;
    }

    @Pointcut("@annotation(allowPermission)")
    public void callAt(AllowPermission allowPermission) {

    }

    @Around(value = "callAt(allowPermission)", argNames = "jp,allowPermission")
    public Object around(ProceedingJoinPoint jp, AllowPermission allowPermission) throws Throwable {
        CodeMessage<String> result = securityApiClient.checkPermissionsAllowed(
                new UserRoleRequest(MDC.get(Constants.KEY_MDC_AUTH_TOKEN),
                        Arrays.asList(allowPermission.roles())));
        if (!result.getCode().equals(HttpStatus.NO_CONTENT.value())) {
            throw new ActionForbiddenException(result.getMessage());
        }
        return jp.proceed();
    }

}
