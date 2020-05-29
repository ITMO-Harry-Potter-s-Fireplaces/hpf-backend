package ru.fireplaces.harrypotter.itmo.utils.response;

import javax.servlet.http.HttpServletResponse;

/**
 * Response changer class with static method to add
 * CORS headers.
 *
 * @author seniorkot
 */
public class ResponseChanger {

    /**
     * Adds CORS headers to {@link HttpServletResponse} object.
     *
     * @param response HTTP Response object
     */
    public static void addCORSHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers",
                "Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, " +
                        "Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS");
    }
}
