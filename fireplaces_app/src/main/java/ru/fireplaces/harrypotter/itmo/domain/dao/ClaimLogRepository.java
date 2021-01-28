package ru.fireplaces.harrypotter.itmo.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.domain.model.ClaimLog;

/**
 * {@link ClaimLog} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface ClaimLogRepository extends JpaRepository<ClaimLog, Long> {

    /**
     * Fetches all {@link ClaimLog} entities by status.
     *
     * @param pageable Pageable params
     * @param claim Claim
     * @return {@link Page} with fetched {@link ClaimLog} entities
     */
    Page<ClaimLog> findAllByClaim(Pageable pageable, Claim claim);
}
