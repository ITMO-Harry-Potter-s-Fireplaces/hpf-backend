package ru.fireplaces.harrypotter.itmo.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.domain.model.ClaimReport;
import ru.fireplaces.harrypotter.itmo.domain.model.User;

/**
 * {@link ClaimReport} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface ClaimReportRepository extends JpaRepository<ClaimReport, Long> {

    /**
     * Checks if claim report already exists.
     *
     * @param claim Claim
     * @param reporter Reporter
     * @return true - claim exists
     */
    boolean existsByClaimAndReporter(Claim claim, User reporter);
}
