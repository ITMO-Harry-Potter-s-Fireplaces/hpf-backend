package ru.fireplaces.harrypotter.itmo.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.fireplaces.harrypotter.itmo.security.config.SecurityKeysProperties;

/**
 * Application main class.
 *
 * @author seniorkot
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableConfigurationProperties({SecurityKeysProperties.class})
public class AuthServiceApplication {

    /**
     * Main method.<br>
     * Runs Spring Boot application.
     *
     * @param args App arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
