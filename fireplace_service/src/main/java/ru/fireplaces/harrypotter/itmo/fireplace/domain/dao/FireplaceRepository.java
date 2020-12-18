package ru.fireplaces.harrypotter.itmo.fireplace.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;

import java.util.Optional;

/**
 * {@link Fireplace} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface FireplaceRepository extends JpaRepository<Fireplace, Long> {

    /**
     * Fetches all nearest {@link Fireplace} entities by coordinates.
     *
     * @param pageable {@link Pageable} params
     * @param lat Latitude
     * @param lng Longitude
     * @param radius Search radius
     * @return {@link Page} with fetched {@link Fireplace} entities
     */
    @Query("SELECT f FROM Fireplace f WHERE (6371.0 * FUNCTION('acos', " +
            "FUNCTION('cos', FUNCTION('radians', :lat)) *" +
            "FUNCTION('cos', FUNCTION('radians', f.lat)) *" +
            "FUNCTION('cos', FUNCTION('radians', f.lng) - FUNCTION('radians', :lng)) +" +
            "FUNCTION('sin', FUNCTION('radians', :lat)) * " +
            "FUNCTION('sin', FUNCTION('radians', f.lat)))) <= :radius")
    Page<Fireplace> findAllNearest(Pageable pageable,
                                   @Param("lat") Float lat,
                                   @Param("lng") Float lng,
                                   @Param("radius") Double radius);

    /**
     * Returns bool value of {@link Fireplace} existence by its coords.
     *
     * @param lat Latitude
     * @param lng Longitude
     * @return true - {@link Fireplace} exists
     */
    boolean existsByLatAndLng(Float lat, Float lng);

    /**
     * Returns {@link Fireplace} entity by its coords.
     *
     * @param lat Latitude
     * @param lng Longitude
     * @return fetched {@link Fireplace} entity
     */
    Optional<Fireplace> findByLatAndLng(Float lat, Float lng);
}
