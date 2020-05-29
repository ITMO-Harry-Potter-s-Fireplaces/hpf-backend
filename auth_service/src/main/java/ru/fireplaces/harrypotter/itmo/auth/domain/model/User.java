package ru.fireplaces.harrypotter.itmo.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * User entity class.
 *
 * @author LilSmile, seniorkot
 */
@Data
@Entity
@Table(name = "users")
public class User {

    /**
     * User ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User email.
     */
    @Column(nullable = false, unique = true, length = 127)
    private String email;

    /**
     * User password.
     */
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    /**
     * User first name.
     */
    @Column(nullable = false, length = 63)
    private String name;

    /**
     * User last name.
     */
    @Column(nullable = false, length = 63)
    private String surname;

}
