package ru.fireplaces.harrypotter.itmo.auth.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.Role;

/**
 * {@link Role} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Returns bool value of {@link Role} existence by its name.
     *
     * @param name Role name
     * @return true - {@link Role} exists.
     */
    boolean existsByName(String name);
}
