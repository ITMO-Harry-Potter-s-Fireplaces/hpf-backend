package ru.fireplaces.harrypotter.itmo.utils.enums;

/**
 * User role enum.
 *
 * @author seniorkot
 */
public enum Role {
    ADMIN(0),
    MODERATOR(1),
    MINISTER(2),
    USER(3);

    private final Integer value;

    Role(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case ADMIN:
                return "Admin";
            case MODERATOR:
                return "Moderator";
            case MINISTER:
                return "Minister";
            case USER:
                return "User";
            default:
                return super.toString();
        }
    }
}
