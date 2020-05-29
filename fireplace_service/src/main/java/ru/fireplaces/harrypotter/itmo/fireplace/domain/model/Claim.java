package ru.fireplaces.harrypotter.itmo.fireplace.domain.model;

import lombok.Data;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;
import java.util.Date;

/**
 * Claim entity class.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "claim")
public class Claim implements CopyFromRequest<ClaimRequest> {

    /**
     * Claim ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Departure fireplace.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id")
    private Fireplace departure;

    /**
     * Departure arrival.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_id")
    private Fireplace arrival;

    /**
     * Departure date and time.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "departure_time")
    private Date departureTime;

    /**
     * User ID.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Override
    public void copy(ClaimRequest request) {
        this.departure = request.getDeparture();
        this.arrival = request.getArrival();
        this.departureTime = request.getDepartureTime();
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $departure = this.getDeparture();
        result = result * PRIME + ($departure == null ? 43 : $departure.hashCode());
        final Object $arrival = this.getArrival();
        result = result * PRIME + ($arrival == null ? 43 : $arrival.hashCode());
        final Object $departureTime = this.getDepartureTime();
        result = result * PRIME + ($departureTime == null ? 43 : $departureTime.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Claim(id=" + this.getId()
                + ", departureId=" + this.getDeparture().getId()
                + ", arrivalId=" + this.getArrival().getId()
                + ", departureTime=" + this.getDepartureTime()
                + ", userId=" + this.getUserId() + ")";
    }
}
