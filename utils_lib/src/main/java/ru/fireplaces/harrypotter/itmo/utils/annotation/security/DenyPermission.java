package ru.fireplaces.harrypotter.itmo.utils.annotation.security;

import ru.fireplaces.harrypotter.itmo.utils.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to restrict method execution to users
 * with specific roles.
 *
 * @author seniorkot
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DenyPermission {

    Role[] roles();
}
