package ru.fireplaces.harrypotter.itmo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import ru.fireplaces.harrypotter.itmo.domain.dao.ClaimRepository;
import ru.fireplaces.harrypotter.itmo.domain.dao.FireplaceRepository;
import ru.fireplaces.harrypotter.itmo.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.domain.enums.ClaimStatus;
import ru.fireplaces.harrypotter.itmo.domain.model.Claim;
import ru.fireplaces.harrypotter.itmo.domain.model.Fireplace;
import ru.fireplaces.harrypotter.itmo.domain.model.User;
import ru.fireplaces.harrypotter.itmo.domain.model.request.ClaimRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.FireplaceRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private UserRepository userRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private FireplaceRepository fireplaceRepository;

    @Test
    public void registrationTest() throws Exception {
        // Create request content
        UserRequest request = new UserRequest();
        request.setEmail("integration@test.com");
        request.setPassword("integrationTest");
        request.setName("TestName");
        request.setSurname("TestSurname");
        request.setMiddleName("TestMiddleName");
        request.setDateOfBirth(LocalDate.of(2020, 1, 1));

        // Perform request and check response
        mockMvc.perform(post("/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())));

        // Check if created user exists and has correct name and role
        User user = userRepository.findByEmail("integration@test.com").orElse(null);
        assertThat(user, notNullValue());
        assertThat(user.getName(), equalTo("TestName"));
        assertThat(user.getRole(), equalTo(Role.USER));
    }

    @Test
    @Sql(scripts = "classpath:data.sql")
    public void securityTest() throws Exception {
        String userResponseStr = mockMvc.perform(post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@user.com\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andReturn().getResponse().getContentAsString();
        String userToken = JsonPath.parse(userResponseStr).read("$.message.token");

        String ministerResponseStr = mockMvc.perform(post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"minister@minister.com\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andReturn().getResponse().getContentAsString();
        String ministerToken = JsonPath.parse(ministerResponseStr).read("$.message.token");

        // Perform getUsers request with user rights
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.FORBIDDEN.value())));

        // Perform getUsers request with admin rights
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", ministerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())));

        // Create fireplace to perform fireplaces request
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

        // Perform createFireplace request with minister rights
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

    @Test
    public void createClaimsTest() throws Exception {
        String userResponseStr = mockMvc.perform(post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@user.com\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andReturn().getResponse().getContentAsString();
        String token = JsonPath.parse(userResponseStr).read("$.message.token");

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
}
