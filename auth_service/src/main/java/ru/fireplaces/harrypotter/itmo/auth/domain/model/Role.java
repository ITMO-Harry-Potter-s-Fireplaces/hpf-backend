package ru.fireplaces.harrypotter.itmo.auth.domain.model;

import lombok.Data;
import org.springframework.util.StringUtils;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.RoleRequest;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.CopyFromRequest;

import javax.persistence.*;

/**
 * User role entity class.
 *
 * @author seniorkot
 */
@Data
@Entity
@Table(name = "roles")
public class Role implements CopyFromRequest<RoleRequest> {

    /**
     * Role ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Role name.
     */
    @Column(nullable = false, unique = true, length = 63)
    private String name;

    @Override
    public void copy(RoleRequest request) {
        this.name = request.getName();
    }

    @Override
    public void update(RoleRequest request) {
        this.name = !StringUtils.isEmpty(request.getName())
                ? request.getName() : this.name;

    }

    @Override
    public String toString() {
        return "Role(id=" + this.getId()
                + ", name=" + this.getName() + ")";
    }
}
