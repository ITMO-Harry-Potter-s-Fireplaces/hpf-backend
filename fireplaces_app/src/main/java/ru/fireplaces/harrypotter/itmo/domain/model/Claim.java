package ru.fireplaces.harrypotter.itmo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
     * User.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Claim status.
     */
    @Column(nullable = false)
    private ClaimStatus status;

    /**
     * Departure latitude.
     */
    @JsonIgnore
    @Column(name = "departure_lat", nullable = false)
    private Float departureLat;

    /**
     * Departure longitude.
     */
    @JsonIgnore
    @Column(name = "departure_lng", nullable = false)
    private Float departureLng;

    /**
     * Arrival latitude.
     */
    @JsonIgnore
    @Column(name = "arrival_lat", nullable = false)
    private Float arrivalLat;

    /**
     * Arrival longitude.
     */
    @JsonIgnore
    @Column(name = "arrival_lng", nullable = false)
    private Float arrivalLng;

    /**
     * Departure coordinates.
     */
    @Transient
    private Coordinates departure;

    /**
     * Arrival coordinates.
     */
    @Transient
    private Coordinates arrival;

    /**
     * Departure fireplace.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_fp_id")
    private Fireplace departureFireplace;

    /**
     * Arrival fireplace.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_fp_id")
    private Fireplace arrivalFireplace;

    /**
     * Departure date and time.
     */
    @JsonFormat(pattern="dd.MM.yyyy")
    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    /**
     * Reports count.
     */
    @Column(name = "reports_count", nullable = false)
    private Long reportsCount;

    /**
     * Claim reports.
     */
    @JsonIgnoreProperties(value = "claim")
    @OneToMany(mappedBy = "claim")
    private List<ClaimReport> reports;

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
        this.reportsCount = 0L;
    }

    @Override
    public void copy(ClaimRequest request) {
        this.departure = request.getDeparture();
        this.arrival = request.getArrival();
        this.travelDate = request.getTravelDate();
        this.departureLat = departure.getLat();
        this.departureLng = departure.getLng();
        this.arrivalLat = arrival.getLat();
        this.arrivalLng = arrival.getLng();
    }

    @Override
    public void update(ClaimRequest request) {
        this.departure = request.getDeparture() != null
                ? request.getDeparture() : this.departure;
        this.arrival = request.getArrival() != null
                ? request.getArrival() : this.arrival;
        this.travelDate = request.getTravelDate() != null
                ? request.getTravelDate() : this.travelDate;
        this.departureLat = departure.getLat();
        this.departureLng = departure.getLng();
        this.arrivalLat = arrival.getLat();
        this.arrivalLng = arrival.getLng();
    }

    @Override
    public String toString() {
        return "Claim(id=" + this.getId()
                + ", userId=" + this.getUser().getId()
                + ", status=" + this.getStatus()
                + ", departure=" + this.getDeparture()
                + ", arrival=" + this.getArrival()
                + ", travelDate=" + this.getTravelDate() + ")";
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
