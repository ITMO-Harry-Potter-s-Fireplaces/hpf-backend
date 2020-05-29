package ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request;

import lombok.Data;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace}
 * POJO. Used in request bodies.
 *
 * @author seniorkot
 */
@Data
public class FireplaceRequest implements RequestRequiredFields {

    /**
     * Fireplace longitude.
     */
    private Float lng;

    /**
     * Fireplace latitude.
     */
    private Float lat;

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (this.lng == null) {
            list.add("lng");
        }
        if (this.lat == null) {
            list.add("lat");
        }
        return list;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $lng = this.getLng();
        result = result * PRIME + ($lng == null ? 43 : $lng.hashCode());
        final Object $lat = this.getLat();
        result = result * PRIME + ($lat == null ? 43 : $lat.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "FireplaceRequest(lng=" + this.getLng()
                + ", lat=" + this.getLat() + ")";
    }
}
