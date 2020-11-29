package ru.fireplaces.harrypotter.itmo.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * User entity class.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "users")
public class User implements CopyFromRequest<UserRequest> {

    /**
     * User ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User role.
     */
    @Column(nullable = false)
    private Role role;

    /**
     * User email.
     */
    @Column(nullable = false, unique = true, length = 127)
    private String email;

    /**
     * User password.
     */
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    /**
     * User first name.
     */
    @Column(length = 63, nullable = false)
    private String name;

    /**
     * User last name.
     */
    @Column(length = 63, nullable = false)
    private String surname;

    /**
     * User middle name.
     */
    @Column(name = "middle_name", length = 63)
    private String middleName;

    /**
     * User date of birth.
     */
    @JsonFormat(pattern="dd.MM.yyyy")
    @Column(name = "date_birth", nullable = false)
    private LocalDate dateOfBirth;

    /**
     * Is user active.
     */
    @Column
    private Boolean active;

    /**
     * Empty constructor for active user creation.
     */
    public User() {
        this.active = true;
    }

    @Override
    public void copy(UserRequest request) {
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.role = request.getRole();
        this.name = request.getName();
        this.surname = request.getSurname();
        this.middleName = request.getMiddleName();
        this.dateOfBirth = request.getDateOfBirth();
    }

    @Override
    public void update(UserRequest request) {
        this.email = !StringUtils.isEmpty(request.getEmail())
                ? request.getEmail() : this.email;
        this.password = !StringUtils.isEmpty(request.getPassword())
                ? request.getPassword() : this.password;
        this.role = request.getRole() != null
                ? request.getRole() : this.role;
        this.name = !StringUtils.isEmpty(request.getName())
                ? request.getName() : this.name;
        this.surname = !StringUtils.isEmpty(request.getSurname())
                ? request.getSurname() : this.surname;
        this.middleName = !StringUtils.isEmpty(request.getMiddleName())
                ? request.getMiddleName() : this.middleName;
        this.dateOfBirth = request.getDateOfBirth() != null
                ? request.getDateOfBirth() : this.dateOfBirth;
    }

    @Override
    public String toString() {
        return "User(id=" + this.getId()
                + ", role=" + this.getRole()
                + ", email=" + this.getEmail()
                + ", name=" + this.getName()
                + ", surname=" + this.getSurname()
                + ", middleName=" + this.getMiddleName()
                + ", dateOfBirth=" + this.getDateOfBirth()
                + ", active=" + this.getActive() + ")";
    }
}
