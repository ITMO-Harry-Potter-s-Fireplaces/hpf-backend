package ru.fireplaces.harrypotter.itmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.fireplaces.harrypotter.itmo.config.SecurityKeysProperties;

/**
 * Application main class.
 *
 * @author seniorkot
 */
@SpringBootApplication
@EnableConfigurationProperties({SecurityKeysProperties.class})
public class FireplacesApplication {

    /**
     * Main method.<br>
     * Runs Spring Boot application.
     *
     * @param args App arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FireplacesApplication.class, args);
    }
}
