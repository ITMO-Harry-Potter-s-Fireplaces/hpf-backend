package ru.fireplaces.harrypotter.itmo.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;
import java.util.Date;

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
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
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
    @Column(length = 63)
    private String name;

    /**
     * User last name.
     */
    @Column(length = 63)
    private String surname;

    /**
     * User middle name.
     */
    @Column(name = "middle_name", length = 63)
    private String middleName;

    /**
     * User date of birth.
     */
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd.MM.yyyy")
    @Column(name = "date_birth")
    private Date dateOfBirth;

    /**
     * Is user active.
     */
    @Column
    private Boolean active;

    /**
     * JSON Getter to return role ID.
     *
     * @return {@link Role} ID
     */
    @JsonGetter("roleId")
    public Long getRoleId() {
        return role.getId();
    }

    /**
     * JSON Getter to return role name.
     *
     * @return {@link Role} name
     */
    @JsonGetter("roleName")
    public String getRoleName() {
        return role.getName();
    }

    /**
     * Empty constructor for active user creation.
     */
    public User() {
        this.active = true;
    }

    @Override
    public void copy(UserRequest request) {
        this.email = request.getEmail();
        if (!StringUtils.isEmpty(request.getPassword())) {
            this.password = request.getPassword();
        }
        if (request.getRole() != null) {
            this.role = request.getRole();
        }
        this.name = request.getName();
        this.surname = request.getSurname();
        this.middleName = request.getMiddleName();
        this.dateOfBirth = request.getDateOfBirth();
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $surname = this.getSurname();
        result = result * PRIME + ($surname == null ? 43 : $surname.hashCode());
        final Object $middleName = this.getMiddleName();
        result = result * PRIME + ($middleName == null ? 43 : $middleName.hashCode());
        final Object $dateOfBirth = this.getDateOfBirth();
        result = result * PRIME + ($dateOfBirth == null ? 43 : $dateOfBirth.hashCode());
        final Object $active = this.getActive();
        result = result * PRIME + ($active == null ? 43 : $active.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User(id=" + this.getId()
                + ", roleId=" + this.getRoleId()
                + ", roleName=" + this.getRoleName()
                + ", email=" + this.getEmail()
                + ", name=" + this.getName()
                + ", surname=" + this.getSurname()
                + ", middleName=" + this.getMiddleName()
                + ", dateOfBirth=" + this.getDateOfBirth()
                + ", active=" + this.getActive() + ")";
    }
}
