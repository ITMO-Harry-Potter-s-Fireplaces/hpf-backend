package ru.fireplaces.harrypotter.itmo.security;

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
import ru.fireplaces.harrypotter.itmo.security.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.security.domain.model.User;
import ru.fireplaces.harrypotter.itmo.security.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;

import java.time.LocalDate;

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

        String adminResponseStr = mockMvc.perform(post("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@admin.com\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andReturn().getResponse().getContentAsString();
        String adminToken = JsonPath.parse(adminResponseStr).read("$.message.token");

        // Perform getUsers request with user rights
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.FORBIDDEN.value())));

        // Perform getUsers request with user rights
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())));
    }
}
