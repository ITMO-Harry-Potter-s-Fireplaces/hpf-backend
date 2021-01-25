package ru.fireplaces.harrypotter.itmo.domain.model;

import lombok.Data;
import ru.fireplaces.harrypotter.itmo.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;

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