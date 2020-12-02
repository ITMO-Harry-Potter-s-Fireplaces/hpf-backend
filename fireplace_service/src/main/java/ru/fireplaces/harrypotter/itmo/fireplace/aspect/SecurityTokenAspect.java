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
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.TokenVerification;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.TokenVerificationRequest;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessage;

/**
 * Aspect to handle {@link TokenVerification} annotation.
 *
 * @author seniorkot
 */
@Aspect
@Component
public class SecurityTokenAspect {

    private final SecurityApiClient securityApiClient;

    @Autowired
    public SecurityTokenAspect(SecurityApiClient securityApiClient) {
        this.securityApiClient = securityApiClient;
    }

    @Pointcut("@annotation(tokenVerification)")
    public void callAt(TokenVerification tokenVerification) {

    }

    @Around(value = "callAt(tokenVerification)", argNames = "jp,tokenVerification")
    public Object around(ProceedingJoinPoint jp, TokenVerification tokenVerification) throws Throwable {
        CodeMessage<String> result = securityApiClient.verifyToken(
                new TokenVerificationRequest(MDC.get(Constants.KEY_MDC_AUTH_TOKEN)));
        if (!result.getCode().equals(HttpStatus.NO_CONTENT.value())) {
            throw new ActionForbiddenException(result.getMessage());
        }
        return jp.proceed();
    }


}
