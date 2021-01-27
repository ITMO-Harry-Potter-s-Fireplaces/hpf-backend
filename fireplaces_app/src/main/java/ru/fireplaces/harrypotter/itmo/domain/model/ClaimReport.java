package ru.fireplaces.harrypotter.itmo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Claim report entity.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "claim_reports")
public class ClaimReport {

    /**
     * Claim ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Who reported.
     */
    @JsonIgnoreProperties(value = {"email", "name", "surname", "middleName", "dateOfBirth", "active"})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    /**
     * Reported claim.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id")
    private Claim claim;

    /**
     * Report message.
     */
    @Column(name = "message")
    private String message;

    /**
     * Date and time of creation.
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:s")
    @Column(name = "report_time", nullable = false)
    private LocalDateTime reportTime;

    /**
     * Pre Persist private method to set up report creation time.
     */
    @PrePersist
    private void prePersist() {
        this.reportTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ClaimReport(id=" + this.getId()
                + ", reporterId=" + this.getReporter().getId()
                + ", claimId=" + this.getClaim().getId()
                + ", message=" + this.getMessage()
                + ", reportTime=" + this.getReportTime() + ")";
    }
}
