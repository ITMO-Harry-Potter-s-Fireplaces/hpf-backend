package ru.fireplaces.harrypotter.itmo.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Auth service key properties.
 *
 * @author seniorkot
 */
@Data
@ConfigurationProperties("security-keys")
public class SecurityKeysProperties {

    /**
     * Path to Public Key.
     */
    private String publicKeyLocation;

    /**
     * Path to Private Key.
     */
    private String privateKeyLocation;
}
