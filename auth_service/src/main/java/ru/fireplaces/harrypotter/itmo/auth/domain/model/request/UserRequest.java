package ru.fireplaces.harrypotter.itmo.auth.domain.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.Role;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ru.fireplaces.harrypotter.itmo.auth.domain.model.User}
 * POJO. Used in request bodies.
 *
 * @author seniorkot
 */
@Data
public class UserRequest implements RequestRequiredFields {

    /**
     * User email.
     */
    private String email;

    /**
     * User password.
     */
    private String password;

    /**
     * User role ID.
     */
    @JsonSetter("role")
    private Long roleId;

    /**
     * User first name.
     */
    private String name;

    /**
     * User last name.
     */
    private String surname;

    /**
     * User middle name.
     */
    private String middleName;

    /**
     * User date of birth.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfBirth;

    /**
     * User role (to copy).
     */
    @JsonIgnore
    private Role role;

    /**
     * Returns a password encoded via MD5 algorithm.
     *
     * @return Encoded password
     */
    public String getPassword() {
        return this.password != null ? DigestUtils.md5Hex(this.password) : null;
    }

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(this.email)) {
            list.add("email");
        }
        if (StringUtils.isEmpty(this.name)) {
            list.add("name");
        }
        if (StringUtils.isEmpty(this.surname)) {
            list.add("surname");
        }
        if (StringUtils.isEmpty(this.middleName)) {
            list.add("middleName");
        }
        if (this.dateOfBirth == null) {
            list.add("dateOfBirth");
        }
        return list;
    }

    @Override
    public String toString() {
        return "UserRequest(email=" + this.getEmail()
                + ", roleId=" + this.getRoleId()
                + ", name=" + this.getName()
                + ", surname=" + this.getSurname()
                + ", middleName=" + this.getMiddleName()
                + ", dateOfBirth=" + this.getDateOfBirth() + ")";
    }
}
