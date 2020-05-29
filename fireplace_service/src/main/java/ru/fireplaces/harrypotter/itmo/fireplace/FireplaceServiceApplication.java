package ru.fireplaces.harrypotter.itmo.fireplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application main class.
 *
 * @author seniorkot
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FireplaceServiceApplication {

    /**
     * Main method.<br>
     * Runs Spring Boot application.
     *
     * @param args App arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FireplaceServiceApplication.class, args);
    }
}
