package ru.fireplaces.harrypotter.itmo.domain.enums;

/**
 * Claim status enum.
 *
 * @author seniorkot
 */
public enum ClaimStatus {
    CREATED(0),
    APPROVED(1),
    REJECTED(2),
    COMPLETED(3),
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
            case REJECTED:
                return "Rejected";
            case COMPLETED:
                return "Completed";
            case CANCELLED:
                return "Cancelled";
            default:
                return super.toString();
        }
    }
}
