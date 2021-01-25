package ru.fireplaces.harrypotter.itmo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.fireplaces.harrypotter.itmo.utils.response.ResponseChanger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Response Filter.
 *
 * @author seniorkot
 */
@Configuration
public class ResponseFilter extends OncePerRequestFilter {

    /**
     * Adds headers to response.
     *
     * @param request HTTP Request object
     * @param response HTTP Response object
     * @param filterChain Filter chain
     * @throws ServletException Servlet exception
     * @throws IOException I/O Exception
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain)  throws ServletException, IOException {
        ResponseChanger.addCORSHeaders(response);
        filterChain.doFilter(request, response);
    }
}
