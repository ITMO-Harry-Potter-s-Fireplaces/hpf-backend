package ru.fireplaces.harrypotter.itmo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Authentication log entity class.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "auth_logs")
public class AuthLog {

    /**
     * Log ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User.
     */
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Date and time.
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    /**
     * User remote address.
     */
    @Column(name = "address", nullable = false)
    private String address;

    /**
     * Is attempt to log in successful.
     */
    @Column(name = "success", nullable = false)
    private Boolean success;

    /**
     * Empty constructor.
     */
    public AuthLog() {

    }

    /**
     * Parametrized constructor.
     *
     * @param user User entity
     * @param address User remote address
     * @param success Successful or not
     */
    public AuthLog(User user, String address, Boolean success) {
        this.user = user;
        this.address = address;
        this.success = success;
    }

    /**
     * Pre Persist private method to set up creation time
     */
    @PrePersist
    private void prePersist() {
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "AuthLog(id=" + this.getId()
                + ", userId=" + this.getUser().getId()
                + ", dateTime=" + this.getDateTime()
                + ", address=" + this.getAddress()
                + ", success=" + this.getSuccess() + ")";
    }
}
