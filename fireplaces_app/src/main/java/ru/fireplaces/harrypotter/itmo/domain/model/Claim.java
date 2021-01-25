package ru.fireplaces.harrypotter.itmo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Claim entity class.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "claims")
public class Claim implements CopyFromRequest<ClaimRequest> {

    /**
     * Claim ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Claim status.
     */
    @Column(nullable = false)
    private ClaimStatus status;

    /**
     * Departure fireplace.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id")
    private Fireplace departure;

    /**
     * Arrival fireplace.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_id")
    private Fireplace arrival;

    /**
     * Departure date and time.
     */
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    /**
     * User.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Date and time of creation.
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    /**
     * Date and time of last modification.
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @Column(name = "modified", nullable = false)
    private LocalDateTime modified;

    public Claim() {
        this.status = ClaimStatus.CREATED;
    }

    @Override
    public void copy(ClaimRequest request) {
        this.departure = request.getDeparture();
        this.arrival = request.getArrival();
        this.departureTime = request.getDepartureTime();
    }

    @Override
    public void update(ClaimRequest request) {
        this.departure = request.getDeparture() != null
                ? request.getDeparture() : this.departure;
        this.arrival = request.getArrival() != null
                ? request.getArrival() : this.arrival;
        this.departureTime = request.getDepartureTime() != null
                ? request.getDepartureTime() : this.departureTime;
    }

    @Override
    public String toString() {
        return "Claim(id=" + this.getId()
                + ", status=" + this.getStatus()
                + ", departureId=" + this.getDeparture().getId()
                + ", arrivalId=" + this.getArrival().getId()
                + ", departureTime=" + this.getDepartureTime()
                + ", userId=" + this.getUser().getId() + ")";
    }

    /**
     * Pre Persist private method to set up creation time
     */
    @PrePersist
    private void prePersist() {
        this.created = LocalDateTime.now();
        this.modified = created;
    }

    /**
     * Pre Update private method to set up last modified time
     */
    @PreUpdate
    private void preUpdate() {
        this.modified = LocalDateTime.now();
    }
}
