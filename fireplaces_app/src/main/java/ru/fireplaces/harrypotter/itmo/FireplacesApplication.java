package ru.fireplaces.harrypotter.itmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.fireplaces.harrypotter.itmo.config.SecurityKeysProperties;

/**
 * Application main class.
 *
 * @author seniorkot
 */
@SpringBootApplication
@EnableAsync
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
