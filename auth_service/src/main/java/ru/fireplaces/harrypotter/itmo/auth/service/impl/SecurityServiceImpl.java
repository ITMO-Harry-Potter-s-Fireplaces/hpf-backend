package ru.fireplaces.harrypotter.itmo.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.config.SecurityKeysProperties;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;
import ru.fireplaces.harrypotter.itmo.auth.service.SecurityService;
import ru.fireplaces.harrypotter.itmo.auth.service.UserService;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionForbiddenException;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;
import ru.fireplaces.harrypotter.itmo.utils.exception.InternalServerErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * Implementation of {@link SecurityService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = SecurityServiceImpl.SERVICE_VALUE)
public class SecurityServiceImpl implements SecurityService {

    public static final String SERVICE_VALUE = "SecurityServiceImpl";

    private static final int TOKEN_EXPIRATION_MILLIS = 2 * 60 * 60 * 1000; // 2 hours

    private static final Logger logger = LogManager.getLogger(SecurityServiceImpl.class);

    private final UserService userService;

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    @Autowired
    public SecurityServiceImpl(SecurityKeysProperties properties,
                               UserService userService) {
        this.userService = userService;

        // Read public and private keys
        RSAPublicKey publicKey = null;
        RSAPrivateKey privateKey = null;
        try {
            publicKey = getPublicKey(properties.getPublicKeyLocation());
            privateKey = getPrivateKey(properties.getPrivateKeyLocation());
        } catch (IOException e) {
            logger.error("Could not read key file:", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm is not available:", e);
        } catch (InvalidKeySpecException e) {
            logger.error("Invalid key specifications:", e);
        }

        this.algorithm = Algorithm.RSA256(publicKey, privateKey);
        this.verifier = JWT.require(algorithm)
                .withIssuer("ITMO-HPF.SecurityService")
                .build();
    }

    @Override
    public String generateToken(@NonNull User user) throws InternalServerErrorException {
        try {
            return JWT.create()
                    .withIssuer("ITMO-HPF.SecurityService")
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRole().getValue())
                    .withClaim("email", user.getEmail())
                    .withClaim("name", user.getName())
                    .withClaim("surname", user.getSurname())
                    .withClaim("middleName", user.getMiddleName())
                    .withClaim("dateOfBirth", user.getDateOfBirth().toString())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_MILLIS))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            logger.error("Error during token generation:", e);
            throw new InternalServerErrorException("Server couldn't generate Auth token");
        }
    }

    @Override
    public boolean validateTokenExpiration(@NonNull String token) throws ActionForbiddenException {
        try {
            verifier.verify(token);
            return true;
        } catch (TokenExpiredException e) {
            return false;
        } catch (BadInputDataException | JWTVerificationException e) {
            logger.error("Error during token verification:", e);
            throw new ActionForbiddenException("Wrong token");
        }
    }

    @Override
    public User authorizeToken(@NonNull String token) throws ActionForbiddenException {
        try {
            DecodedJWT jwt = JWT.decode(token);
            User user = userService.getUser(jwt.getClaim("id").asLong());
            if (user.getRole().getValue().equals(jwt.getClaim("role").asInt())) {
                return user;
            }
            throw new ActionForbiddenException("Roles don't match");
        } catch (BadInputDataException | JWTDecodeException | IllegalArgumentException e) {
            logger.error("Error during token decoding:", e);
        } catch (ActionForbiddenException | EntityNotFoundException e) {
            logger.error("Error during token authorizing:", e);
        }
        throw new ActionForbiddenException("Wrong token");
    }

    /**
     * Private method to return RSA public key.
     *
     * @param fileName Full filename of public key
     * @return RSA public key
     * @throws IOException IO Exception while reading from file
     * @throws NoSuchAlgorithmException Thrown when algorithm is not available in the environment
     * @throws InvalidKeySpecException Thrown when there are invalid key specifications
     */
    private static RSAPublicKey getPublicKey(@NonNull String fileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(fileName));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    /**
     * Private method to return RSA private key.
     *
     * @param fileName Full filename of private key
     * @return RSA private key
     * @throws IOException IO Exception while reading from file
     * @throws NoSuchAlgorithmException Thrown when algorithm is not available in the environment
     * @throws InvalidKeySpecException Thrown when there are invalid key specifications
     */
    private static RSAPrivateKey getPrivateKey(@NonNull String fileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(fileName));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }
}
