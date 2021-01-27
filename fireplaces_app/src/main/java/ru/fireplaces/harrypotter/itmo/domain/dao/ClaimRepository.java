package ru.fireplaces.harrypotter.itmo.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.domain.model.Fireplace;

import java.time.LocalDate;
import java.util.Optional;

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
     * Fetches all {@link Claim} entities by excluded status.
     *
     * @param pageable {@link Pageable} params
     * @param status Claim status
     * @return {@link Page} with fetched {@link Claim} entities
     */
    Page<Claim> findAllByStatusNot(Pageable pageable, ClaimStatus status);

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

    /**
     * Fetches all {@link Claim} entities by excluded status and user ID.
     *
     * @param pageable {@link Pageable} params
     * @param status Claim status
     * @param userId User ID
     * @return {@link Page} with fetched {@link Claim} entities
     */
    Page<Claim> findAllByStatusNotAndUserId(Pageable pageable, ClaimStatus status, Long userId);

    /**
     * Fetches {@link Claim} by departure time and {@link Fireplace}.
     *
     * @param travelDate Departure date
     * @param departure Departure fireplace
     * @return Fetched {@link Claim}
     */
    Optional<Claim> findByTravelDateAndDepartureFireplace(LocalDate travelDate, Fireplace departure);

    /**
     * Fetches {@link Claim} by departure time and destination {@link Fireplace}.
     *
     * @param travelDate Departure date
     * @param arrival Arrival fireplace
     * @return Fetched {@link Claim}
     */
    Optional<Claim> findByTravelDateAndArrivalFireplace(LocalDate travelDate, Fireplace arrival);

    /**
     * Returns bool value of {@link Claim} existence by
     * departure time and destination {@link Fireplace}.
     *
     * @param travelDate Departure date
     * @param departure Departure fireplace
     * @return true - {@link Claim exists}
     */
    boolean existsByTravelDateAndDepartureFireplace(LocalDate travelDate, Fireplace departure);

    /**
     * Returns bool value of {@link Claim} existence by
     * departure time and destination {@link Fireplace}.
     *
     * @param travelDate Departure date
     * @param arrival Arrival fireplace
     * @return true - {@link Claim exists}
     */
    boolean existsByTravelDateAndArrivalFireplace(LocalDate travelDate, Fireplace arrival);
}
