package ru.fireplaces.harrypotter.itmo.auth.domain.model.request;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Log-pass POJO for register and login requests.
 *
 * @author seniorkot
 */
@Data
public class LoginRequest implements RequestRequiredFields {

    /**
     * Login: username or email.
     */
    private String email;

    /**
     * User password.
     */
    private String password;

    /**
     * Returns a password encoded via MD5 algorithm.
     *
     * @return Encoded password
     */
    public String getPassword() {
        return DigestUtils.md5Hex(this.password);
    }

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(this.email)) {
            list.add("email");
        }
        if (StringUtils.isEmpty(this.password)) {
            list.add("password");
        }
        return list;
    }

    @Override
    public String toString() {
        return "LoginRequest(email=" + this.getEmail() + ")";
    }
}
