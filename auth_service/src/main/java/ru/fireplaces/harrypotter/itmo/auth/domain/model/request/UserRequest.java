package ru.fireplaces.harrypotter.itmo.auth.domain.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.Role;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;
import ru.fireplaces.harrypotter.itmo.utils.json.JsonDateDeserializer;

import java.util.ArrayList;
import java.util.Date;
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
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dateOfBirth;

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

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $roleId = this.getRoleId();
        result = result * PRIME + ($roleId == null ? 43 : $roleId.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $surname = this.getSurname();
        result = result * PRIME + ($surname == null ? 43 : $surname.hashCode());
        final Object $middleName = this.getMiddleName();
        result = result * PRIME + ($middleName == null ? 43 : $middleName.hashCode());
        final Object $dateOfBirth = this.getDateOfBirth();
        result = result * PRIME + ($dateOfBirth == null ? 43 : $dateOfBirth.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "UserRequest(email=" + this.getEmail()
                + ", password=" + this.getPassword()
                + ", roleId=" + this.getRoleId()
                + ", name=" + this.getName()
                + ", surname=" + this.getSurname()
                + ", middleName=" + this.getMiddleName()
                + ", dateOfBirth=" + this.getDateOfBirth() + ")";
    }
}