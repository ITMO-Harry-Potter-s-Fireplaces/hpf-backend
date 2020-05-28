package ru.fireplaces.harrypotter.itmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Application main class.
 *
 * @author seniorkot
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ProxyServerApplication {

    /**
     * Main method.<br>
     * Runs Spring Boot application.
     *
     * @param args App arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ProxyServerApplication.class, args);
    }
}
