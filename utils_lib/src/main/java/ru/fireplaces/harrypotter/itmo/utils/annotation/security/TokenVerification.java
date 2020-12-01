package ru.fireplaces.harrypotter.itmo.utils.annotation.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to run user auth token verification before
 * method execution.
 *
 * @author seniorkot
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TokenVerification {

}
