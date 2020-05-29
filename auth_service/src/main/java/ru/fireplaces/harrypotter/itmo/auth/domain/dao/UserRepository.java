package ru.fireplaces.harrypotter.itmo.auth.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.Role;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * {@link User} JPA repository interface.
 *
 * @author seniorkot
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Fetches all users by active status.
     *
     * @param active Flag: fetch active users or not
     * @return {@link List} with fetched {@link User} entities
     */
    List<User> findAllByActive(Boolean active);

    /**
     * Fetches all users by active status.
     *
     * @param pageable {@link Pageable} params
     * @param active Flag: fetch active users or not
     * @return {@link Page} with fetched {@link User} entities
     */
    Page<User> findAllByActive(Pageable pageable, Boolean active);

    /**
     * Fetches all users by active status and role IDs.
     *
     * @param active Flag: fetch active users or not
     * @param roleIds Role IDs
     * @return {@link List} with fetched {@link User} entities
     */
    List<User> findAllByActiveAndRoleIn(Boolean active, Iterable<Long> roleIds);

    /**
     * Fetches all users by active status and role IDs.
     *
     * @param pageable {@link Pageable} params
     * @param active Flag: fetch active users or not
     * @param roleIds Role IDs
     * @return {@link Page} with fetched {@link User} entities
     */
    Page<User> findAllByActiveAndRoleIn(Pageable pageable, Boolean active, Iterable<Long> roleIds);

    /**
     * Finds {@link User} with specified email address.
     *
     * @param email User email
     * @return {@link User} entity
     */
    Optional<User> findByEmail(String email);

    /**
     * Returns bool value of {@link User} existence by its email.
     *
     * @param email User email
     * @return true - {@link User} exists.
     */
    boolean existsByEmail(String email);

    /**
     * Counts how many users have certain role.
     *
     * @param role {@link Role} object
     * @return the number of {@link User} with such role
     */
    long countByRole(Role role);
}
