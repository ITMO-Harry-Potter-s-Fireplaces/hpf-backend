package ru.fireplaces.harrypotter.itmo.utils.enums;

/**
 * User role enum.
 *
 * @author seniorkot
 */
public enum Role {
    ADMIN,
    MODERATOR,
    USER;

    @Override
    public String toString() {
        switch (this) {
            case ADMIN:
                return "Admin";
            case MODERATOR:
                return "Moderator";
            case USER:
                return "User";
            default:
                return super.toString();
        }
    }
}
