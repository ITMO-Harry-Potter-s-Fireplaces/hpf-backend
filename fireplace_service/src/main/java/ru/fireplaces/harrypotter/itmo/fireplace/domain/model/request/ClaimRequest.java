package ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Claim}
 * POJO. Used in request bodies.
 *
 * @author seniorkot
 */
@Data
public class ClaimRequest implements RequestRequiredFields {

    /**
     * Departure fireplace ID.
     */
    private Long departureId;

    /**
     * Arrival fireplace ID.
     */
    private Long arrivalId;

    /**
     * Departure date and time.
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime departureTime;

    /**
     * Departure fireplace (to copy).
     */
    @JsonIgnore
    private Fireplace departure;

    /**
     * Departure arrival (to copy).
     */
    @JsonIgnore
    private Fireplace arrival;

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (this.departureId == null) {
            list.add("departureId");
        }
        if (this.arrivalId == null) {
            list.add("arrivalId");
        }
        if (this.departureTime == null) {
            list.add("departureTime");
        }
        return list;
    }

    @Override
    public String toString() {
        return "ClaimRequest(departureId=" + this.getDepartureId()
                + ", arrivalId=" + this.getArrivalId()
                + ", departureTime=" + this.getDepartureTime() + ")";
    }
}
