package ru.fireplaces.harrypotter.itmo.fireplace.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;

/**
 * {@link Fireplace} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface FireplaceRepository extends JpaRepository<Fireplace, Long> {

    @Query("SELECT f FROM Fireplace f ORDER BY SQRT((f.lng * f.lng + :lng) + (f.lat * f.lat + :lat))")
    Page<Fireplace> findAllNearest(Pageable pageable, @Param("lng") Float lng, @Param("lat") Float lat);
}
