package ru.fireplaces.harrypotter.itmo.domain.model;

import lombok.Data;

/**
 * Coordinates POJO.
 *
 * @author seniorkot
 */
@Data
public class Coordinates {

    /**
     * Latitude.
     */
    private Float lat;

    /**
     * Longitude.
     */
    private Float lng;

    /**
     * Empty constructor.
     */
    public Coordinates() {

    }

    /**
     * Parametrized constructor.
     *
     * @param lat Latitude
     * @param lng Longitude
     */
    public Coordinates(Float lat, Float lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Coordinates(lat=" + this.getLat()
                + ", lng=" + this.getLng() + ")";
    }
}
