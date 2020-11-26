package ru.fireplaces.harrypotter.itmo.auth.domain.model.request;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ru.fireplaces.harrypotter.itmo.auth.domain.model.Role}
 * POJO. Used in request bodies.
 *
 * @author seniorkot
 */
@Data
public class RoleRequest implements RequestRequiredFields {

    /**
     * Role name.
     */
    private String name;

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(name)) {
            list.add("name");
        }
        return list;
    }

    @Override
    public String toString() {
        return "RoleRequest(name=" + this.getName() + ")";
    }
}
