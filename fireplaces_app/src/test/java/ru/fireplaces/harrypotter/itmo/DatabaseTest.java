package ru.fireplaces.harrypotter.itmo;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fireplaces.harrypotter.itmo.domain.dao.ClaimRepository;
import ru.fireplaces.harrypotter.itmo.domain.dao.FireplaceRepository;
import ru.fireplaces.harrypotter.itmo.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.domain.model.User;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Database and JpaRepository test.
 *
 * @author seniorkot
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FireplaceRepository fireplaceRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void databaseUsersSaveAndFindTest() {
        // Create entity
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);
        user.setPassword(DigestUtils.md5Hex("123"));
        user.setName("databaseSaveAndFindTest");
        user.setSurname("test");
        user.setMiddleName("test");
        user.setDateOfBirth(LocalDate.of(2020, 1, 1));
        user.setRole(Role.USER);

        // Save user and get saved entity via repository
        entityManager.persist(user);
        entityManager.flush();
        User foundUser = userRepository.findByEmail(email).orElse(null);

        // Compare created and found ones
        assertThat(foundUser, notNullValue());
        assertThat(foundUser.getName(), equalTo(user.getName()));

        // Create fireplaces
        Fireplace fireplace1 = new Fireplace();
        Fireplace fireplace2 = new Fireplace();

        fireplace1.setLat(14.88f);
        fireplace1.setLng(2.28f);
        fireplace1.setDescription("Fireplace number one");

        fireplace2.setLat(2.28f);
        fireplace2.setLng(14.88f);
        fireplace2.setDescription("Fireplace number two");

        // Save fireplaces
        entityManager.persist(fireplace1);
        entityManager.persist(fireplace2);
        entityManager.flush();

        // Find created fireplaces by coords
        Fireplace foundFp1 = fireplaceRepository.findByLatAndLng(14.88f, 2.28f).orElse(null);
        Fireplace foundFp2 = fireplaceRepository.findByLatAndLng(2.28f, 14.88f).orElse(null);

        // Compare created and found fireplaces
        assertThat(foundFp1, notNullValue());
        assertThat(foundFp2, notNullValue());
        assertThat(foundFp1.getDescription(), equalTo(fireplace1.getDescription()));
        assertThat(foundFp2.getDescription(), equalTo(fireplace2.getDescription()));

        // Create claim and save
        LocalDateTime departureTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        Claim claim = new Claim();
        claim.setDeparture(foundFp1);
        claim.setArrival(foundFp2);
        claim.setDepartureTime(departureTime);
        claim.setUser(foundUser);
        entityManager.persist(claim);
        entityManager.flush();

        // Find created claim
        Claim foundClaim = claimRepository.findByDepartureTimeAndDeparture(departureTime, foundFp1).orElse(null);

        // Compare created and found claims
        assertThat(foundClaim, notNullValue());
        assertThat(foundClaim.getArrival(), equalTo(foundFp2));
    }
}
