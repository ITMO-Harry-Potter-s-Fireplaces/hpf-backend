package ru.fireplaces.harrypotter.itmo.fireplace.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;

/**
 * {@link Fireplace} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface FireplaceRepository extends JpaRepository<Fireplace, Long> {

}
