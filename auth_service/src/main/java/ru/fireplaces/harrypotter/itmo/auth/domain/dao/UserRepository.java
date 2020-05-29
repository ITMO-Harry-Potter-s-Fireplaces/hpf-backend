package ru.fireplaces.harrypotter.itmo.auth.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;

/**
 * {@link User} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
