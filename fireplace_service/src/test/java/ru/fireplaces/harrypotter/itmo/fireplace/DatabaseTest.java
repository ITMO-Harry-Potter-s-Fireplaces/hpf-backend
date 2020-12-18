package ru.fireplaces.harrypotter.itmo.fireplace;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.dao.ClaimRepository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.dao.FireplaceRepository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;

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
    private FireplaceRepository fireplaceRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void databaseSaveAndFindTest() {
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
        Fireplace found1 = fireplaceRepository.findByLatAndLng(14.88f, 2.28f).orElse(null);
        Fireplace found2 = fireplaceRepository.findByLatAndLng(2.28f, 14.88f).orElse(null);

        // Compare created and found fireplaces
        assertThat(found1, notNullValue());
        assertThat(found2, notNullValue());
        assertThat(found1.getDescription(), equalTo(fireplace1.getDescription()));
        assertThat(found2.getDescription(), equalTo(fireplace2.getDescription()));

        // Create claim and save
        LocalDateTime departureTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        Claim claim = new Claim();
        claim.setDeparture(found1);
        claim.setArrival(found2);
        claim.setDepartureTime(departureTime);
        claim.setUserId(0L);
        entityManager.persist(claim);
        entityManager.flush();

        // Find created claim
        Claim found = claimRepository.findByDepartureTimeAndDeparture(departureTime, found1).orElse(null);

        // Compare created and found claims
        assertThat(found, notNullValue());
        assertThat(found.getArrival(), equalTo(found2));
    }
}
