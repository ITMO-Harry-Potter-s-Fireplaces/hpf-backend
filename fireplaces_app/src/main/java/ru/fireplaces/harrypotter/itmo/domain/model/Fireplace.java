package ru.fireplaces.harrypotter.itmo.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.fireplaces.harrypotter.itmo.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;
import java.util.List;

/**
 * Fireplace entity class.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "fireplaces")
public class Fireplace implements CopyFromRequest<FireplaceRequest> {

    /**
     * Fireplace ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fireplace longitude.
     */
    @Column(nullable = false)
    private Float lng;

    /**
     * Fireplace latitude.
     */
    @Column(nullable = false)
    private Float lat;

    /**
     * Fireplace description.
     */
    @Column(length = 511)
    private String description;

    /**
     * Fireplace distance from coordinates.
     */
    @Transient
    private Double distance;

    /**
     * Claims count.
     */
    @Transient
    private Long claimsCount;

    /**
     * Claims that use this fireplace as departure point.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "departureFireplace", fetch = FetchType.LAZY)
    private List<Claim> claimsDeparture;

    /**
     * Claims that use this fireplace as arrival point.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "arrivalFireplace", fetch = FetchType.LAZY)
    private List<Claim> claimsArrival;

    @Override
    public void copy(FireplaceRequest request) {
        this.lng = request.getLng();
        this.lat = request.getLat();
        this.description = request.getDescription();
    }

    @Override
    public void update(FireplaceRequest request) {
        this.lng = request.getLng() != null
                ? request.getLng() : this.lng;
        this.lat = request.getLat() != null
                ? request.getLat() : this.lat;
        this.description = request.getDescription() != null
                ? request.getDescription() : this.description;
    }

    @Override
    public String toString() {
        return "Fireplace(id=" + this.getId()
                + ", lng=" + this.getLng()
                + ", lat=" + this.getLat()
                + ", description=" + this.getDescription() + ")";
    }
}
