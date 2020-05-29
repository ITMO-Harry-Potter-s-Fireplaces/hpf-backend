package ru.fireplaces.harrypotter.itmo.fireplace.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Claim;

/**
 * {@link Claim} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

}
