package ru.fireplaces.harrypotter.itmo.fireplace.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Claim;

/**
 * {@link Claim} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    /**
     * Fetches all {@link Claim} entities by status.
     *
     * @param pageable {@link Pageable} params
     * @param status Claim status
     * @return {@link Page} with fetched {@link Claim} entities
     */
    Page<Claim> findAllByStatus(Pageable pageable, ClaimStatus status);

    /**
     * Fetches all {@link Claim} entities by user ID.
     *
     * @param pageable {@link Pageable} params
     * @param userId User ID
     * @return {@link Page} with fetched {@link Claim} entities
     */
    Page<Claim> findAllByUserId(Pageable pageable, Long userId);

    /**
     * Fetches all {@link Claim} entities by status and user ID.
     *
     * @param pageable {@link Pageable} params
     * @param status Claim status
     * @param userId User ID
     * @return {@link Page} with fetched {@link Claim} entities
     */
    Page<Claim> findAllByStatusAndUserId(Pageable pageable, ClaimStatus status, Long userId);
}
