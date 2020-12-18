package ru.fireplaces.harrypotter.itmo.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fireplaces.harrypotter.itmo.security.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.security.domain.model.User;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;

import java.time.LocalDate;

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
    private TestEntityManager entityManager;

    @Test
    public void databaseSaveAndFindTest() {
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
        User found = userRepository.findByEmail(email).orElse(null);

        // Compare created and found ones
        assertThat(found, notNullValue());
        assertThat(found.getName(), equalTo(user.getName()));
    }
}
