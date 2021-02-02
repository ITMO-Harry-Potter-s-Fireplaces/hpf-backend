package ru.fireplaces.harrypotter.itmo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Claim report entity.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "claim_logs")
public class ClaimLog {

    /**
     * Claim log ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Updated claim.
     */
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id")
    private Claim claim;

    /**
     * Who changed claim.
     */
    @JsonIgnoreProperties(value = {"email", "name", "surname", "middleName", "dateOfBirth", "active"})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Previous claim state.
     */
    @Column(name = "prev_state")
    private ClaimStatus prevState;

    /**
     * New claim state.
     */
    @Column(name = "new_state", nullable = false)
    private ClaimStatus newState;

    /**
     * Change state reason.
     */
    @Column(name = "message", length = 511)
    private String message;

    /**
     * Date and time of log.
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * Empty constructor.
     */
    public ClaimLog() {

    }

    /**
     * Parametrized constructor.
     *
     * @param claim Claim
     * @param user User
     * @param prevState Previous state
     * @param newState New state
     */
    public ClaimLog(Claim claim, User user, ClaimStatus prevState, ClaimStatus newState) {
        this.claim = claim;
        this.user = user;
        this.prevState = prevState;
        this.newState = newState;
    }

    /**
     * Pre Persist private method to set up creation time
     */
    @PrePersist
    private void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ClaimLog(id=" + this.getId()
                + ", claimId=" + this.getClaim().getId()
                + ", userId=" + this.getUser().getId()
                + ", prevState=" + this.getPrevState()
                + ", newState=" + this.getNewState()
                + ", message=" + this.getMessage()
                + ", timestamp=" + this.getTimestamp() + ")";
    }
}
