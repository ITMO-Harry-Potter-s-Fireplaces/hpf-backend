package ru.fireplaces.harrypotter.itmo.fireplace;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.dao.ClaimRepository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.dao.FireplaceRepository;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.feign.SecurityApiClient;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration and security tests.
 *
 * @author seniorkot
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityApiClient securityApiClient;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private FireplaceRepository fireplaceRepository;

    @Test
    @Sql(scripts = "classpath:data.sql")
    public void createClaimsTest() throws Exception {
        String token = securityApiClient.login(
                new LoginRequest("user@user.com", "123")).getMessage().getToken();

        LocalDateTime departureTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        Fireplace departure = fireplaceRepository.findById(1L).orElse(null);
        Fireplace arrival = fireplaceRepository.findById(2L).orElse(null);
        assertThat(departure, notNullValue());
        assertThat(arrival, notNullValue());

        ClaimRequest claim1 = new ClaimRequest();
        claim1.setDepartureId(departure.getId());
        claim1.setArrivalId(arrival.getId());
        claim1.setDepartureTime(departureTime);

        // Perform request and check response
        mockMvc.perform(post("/claims")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(claim1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.CREATED.value())));

        // Check if claim was created and has correct status
        Claim claim = claimRepository.findByDepartureTimeAndDeparture(departureTime, departure).orElse(null);
        assertThat(claim, notNullValue());
        assertThat(claim.getStatus(), equalTo(ClaimStatus.CREATED));

        // Try to create claim with booked fireplace at the same time
        claim1.setArrivalId(3L);
        // Perform request and check response
        mockMvc.perform(post("/claims")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(claim1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.UNPROCESSABLE_ENTITY.value())));
    }

    @Test
    public void securityTest() throws Exception {
        String userToken = securityApiClient.login(
                new LoginRequest("user@user.com", "123")).getMessage().getToken();
        String ministerToken = securityApiClient.login(
                new LoginRequest("minister@minister.com", "123")).getMessage().getToken();

        FireplaceRequest newFireplace = new FireplaceRequest();
        newFireplace.setLat(14.88f);
        newFireplace.setLng(2.28f);
        newFireplace.setDescription("Just for lulz");

        // Perform createFireplace request with user rights
        mockMvc.perform(post("/fireplaces")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userToken)
                .content(objectMapper.writeValueAsString(newFireplace)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.FORBIDDEN.value())));

        // Perform createFireplace request with user rights
        mockMvc.perform(post("/fireplaces")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", ministerToken)
                .content(objectMapper.writeValueAsString(newFireplace)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.CREATED.value())));

        // Check if it's really created
        Fireplace created = fireplaceRepository.findByLatAndLng(14.88f, 2.28f).orElse(null);
        assertThat(created, notNullValue());
        assertThat(created.getDescription(), equalTo("Just for lulz"));
    }
}
