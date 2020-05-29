package ru.fireplaces.harrypotter.itmo.auth.domain.model;

import lombok.Data;
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
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Role(id=" + this.getId()
                + ", name=" + this.getName() + ")";
    }
}
