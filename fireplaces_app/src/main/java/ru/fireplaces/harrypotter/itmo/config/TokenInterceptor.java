package ru.fireplaces.harrypotter.itmo.config;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.fireplaces.harrypotter.itmo.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token interceptor which saves Auth token via MDC for further use.
 *
 * @author seniorkot
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

    /**
     * Called before the actual handler is executed to save Auth token
     * via MDC for further use.
     *
     * @param request Request object
     * @param response Response object
     * @param handler Actual handler
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        MDC.put(Constants.KEY_MDC_AUTH_TOKEN, request.getHeader("Authorization"));
        return super.preHandle(request, response, handler);
    }

    /**
     * Called after the handler is executed to clear MDC.
     *
     * @param request Request object
     * @param response Response object
     * @param handler Actual handler
     * @param modelAndView Model and view
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        MDC.clear();
        super.postHandle(request, response, handler, modelAndView);
    }
}

