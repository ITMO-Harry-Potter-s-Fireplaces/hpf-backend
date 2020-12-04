package ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request;

import lombok.Data;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Request POJO to receive user longitude and latitude.
 * Used for {@link ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace}
 * sorting.
 *
 * @author seniorkot
 */
@Data
public class CoordsRequest implements RequestRequiredFields {

    /**
     * User longitude.
     */
    private Float lng;

    /**
     * User latitude.
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
    public String toString() {
        return "CoordsRequest(lng=" + this.getLng()
                + ", lat=" + this.getLat() + ")";
    }
}
