package ru.fireplaces.harrypotter.itmo.utils.domain.model.request;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Token and {@link Role}s POJO. Used in request bodies for
 * {@link ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission}.
 *
 * @author seniorkot
 */
@Data
public class UserRoleRequest implements RequestRequiredFields {

    /**
     * Authorization token.
     */
    private String token;

    /**
     * Roles.
     */
    private List<Role> roles;

    /**
     * Parametrized constructor for user role verification request.
     *
     * @param token Auth token
     * @param roles User roles
     */
    public UserRoleRequest(String token, List<Role> roles) {
        this.token = token;
        this.roles = roles;
    }

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(token)) {
            list.add("token");
        }
        if (CollectionUtils.isEmpty(roles)) {
            list.add("roles");
        }
        return list;
    }
}
