package ru.fireplaces.harrypotter.itmo.fireplace.domain.enums;

/**
 * Claim status enum.
 *
 * @author seniorkot
 */
public enum ClaimStatus {
    CREATED(0),
    APPROVED(1),
    COMPLETED(2),
    DENIED(3),
    CANCELLED(4);

    private final Integer value;

    ClaimStatus(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case CREATED:
                return "Created";
            case APPROVED:
                return "Approved";
            case COMPLETED:
                return "Completed";
            case DENIED:
                return "Denied";
            case CANCELLED:
                return "Cancelled";
            default:
                return super.toString();
        }
    }
}
