package ru.fireplaces.harrypotter.itmo.fireplace.domain.model;

import lombok.Data;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;

/**
 * User entity class.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "users")
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

    @Override
    public void copy(FireplaceRequest request) {
        this.lng = request.getLng();
        this.lat = request.getLat();
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $lng = this.getLng();
        result = result * PRIME + ($lng == null ? 43 : $lng.hashCode());
        final Object $lat = this.getLat();
        result = result * PRIME + ($lat == null ? 43 : $lat.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Fireplace(id=" + this.getId()
                + ", lng=" + this.getLng()
                + ", lat=" + this.getLat() + ")";
    }
}
