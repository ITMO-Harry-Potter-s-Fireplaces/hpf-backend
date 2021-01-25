package ru.fireplaces.harrypotter.itmo.domain.model.request;

import lombok.Data;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Request POJO to receive user longitude and latitude.
 * Used for {@link ru.fireplaces.harrypotter.itmo.domain.model.Fireplace}
 * sorting.
 *
 * @author seniorkot
 */
@Data
public class CoordsRequest implements RequestRequiredFields {

    /**
     * User latitude.
     */
    private Float lat;

    /**
     * User longitude.
     */
    private Float lng;

    /**
     * Search radius.
     */
    private Double radius;

    public CoordsRequest() {

    }

    /**
     * Parametrized constructor for requests.
     *
     * @param lng Longitude
     * @param lat Latitude
     */
    public CoordsRequest(Float lat, Float lng, Double radius) {
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
    }

    public boolean isEmpty() {
        return this.lat == null && this.lng == null && this.radius == null;
    }

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (this.lat == null) {
            list.add("lat");
        }
        if (this.lng == null) {
            list.add("lng");
        }
        if (this.radius == null) {
            list.add("radius");
        }
        return list;
    }

    @Override
    public String toString() {
        return "CoordsRequest(lng=" + this.getLng()
                + ", lat=" + this.getLat() + ")";
    }
}
