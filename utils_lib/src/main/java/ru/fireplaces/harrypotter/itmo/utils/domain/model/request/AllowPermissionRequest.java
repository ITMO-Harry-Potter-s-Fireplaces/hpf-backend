package ru.fireplaces.harrypotter.itmo.utils.domain.model.request;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.fireplaces.harrypotter.itmo.utils.annotation.security.AllowPermission;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AllowPermission} POJO. Used in request bodies.
 *
 * @author seniorkot
 */
@Data
public class AllowPermissionRequest implements RequestRequiredFields {

    /**
     * Authorization token.
     */
    private String token;

    /**
     * Roles.
     */
    private List<Role> roles;

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
