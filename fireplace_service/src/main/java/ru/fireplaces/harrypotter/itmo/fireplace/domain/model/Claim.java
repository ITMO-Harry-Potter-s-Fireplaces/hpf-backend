package ru.fireplaces.harrypotter.itmo.fireplace.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.ClaimRequest;
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
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @Column(name = "departure_time")
    private LocalDateTime departureTime;

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
                + ", departureId=" + this.getDeparture().getId()
                + ", arrivalId=" + this.getArrival().getId()
                + ", departureTime=" + this.getDepartureTime()
                + ", userId=" + this.getUserId() + ")";
    }
}
