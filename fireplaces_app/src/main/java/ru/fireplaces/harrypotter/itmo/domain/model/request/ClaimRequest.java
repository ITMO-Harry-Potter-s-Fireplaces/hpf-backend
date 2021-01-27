package ru.fireplaces.harrypotter.itmo.domain.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import ru.fireplaces.harrypotter.itmo.domain.model.Coordinates;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ru.fireplaces.harrypotter.itmo.domain.model.Claim}
 * POJO. Used in request bodies.
 *
 * @author seniorkot
 */
@Data
public class ClaimRequest implements RequestRequiredFields {

    /**
     * Departure point.
     */
    private Coordinates departure;

    /**
     * Destination point.
     */
    private Coordinates arrival;

    /**
     * Travel date.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate travelDate;

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (this.departure == null) {
            list.add("departure");
        }
        if (this.arrival == null) {
            list.add("arrival");
        }
        if (this.travelDate == null) {
            list.add("travelDate");
        }
        return list;
    }

    @Override
    public String toString() {
        return "ClaimRequest(departure=" + this.getDeparture()
                + ", arrival=" + this.getArrival()
                + ", travelDate=" + this.getTravelDate() + ")";
    }
}
