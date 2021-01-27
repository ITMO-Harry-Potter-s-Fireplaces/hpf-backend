package ru.fireplaces.harrypotter.itmo.domain.model.request;

import lombok.Data;

/**
 * {@link ru.fireplaces.harrypotter.itmo.domain.model.ClaimReport}
 * POJO. Used in request bodies.
 *
 * @author seniorkot
 */
@Data
public class ClaimReportRequest {

    /**
     * Report message.
     */
    private String message;
}
