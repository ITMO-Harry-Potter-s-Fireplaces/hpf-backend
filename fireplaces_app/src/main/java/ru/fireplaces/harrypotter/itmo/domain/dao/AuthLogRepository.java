package ru.fireplaces.harrypotter.itmo.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.domain.model.AuthLog;
import ru.fireplaces.harrypotter.itmo.domain.model.User;

/**
 * {@link AuthLog} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {

    /**
     * Fetches all {@link AuthLog} entities by user ID.
     *
     * @param pageable {@link Pageable} params
     * @param user User entity
     * @return {@link Page} with fetched {@link AuthLog} entities
     */
    Page<AuthLog> findAllByUser(Pageable pageable, User user);
}
