package ru.fireplaces.harrypotter.itmo.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.response.LoginResponse;
import ru.fireplaces.harrypotter.itmo.auth.service.AuthService;
import ru.fireplaces.harrypotter.itmo.auth.service.RoleService;
import ru.fireplaces.harrypotter.itmo.auth.service.SecurityService;
import ru.fireplaces.harrypotter.itmo.utils.exception.*;

import java.util.List;

/**
 * Implementation of {@link AuthService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = AuthServiceImpl.SERVICE_VALUE)
public class AuthServiceImpl implements AuthService {

    public static final String SERVICE_VALUE = "AuthServiceImpl";

    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final RoleService roleService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           SecurityService securityService,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.roleService = roleService;
    }

    @Override
    public LoginResponse register(@NonNull UserRequest request)
            throws EntityAlreadyExistsException, BadInputDataException {
        List<String> blankFields = request.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(LoginRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        User user = new User();
        request.setRole(roleService.getRole(2L));
        user.copy(request);
        User savedUser = userRepository.save(user);
        return new LoginResponse(savedUser.getId(), securityService.generateToken(savedUser));
    }

    @Override
    public LoginResponse login(@NonNull LoginRequest request) throws EntityNotFoundException,
            BadInputDataException, UserUnauthorizedException, ActionForbiddenException {
        List<String> blankFields = request.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(LoginRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No User with email " + request.getEmail() + " found"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new UserUnauthorizedException("Incorrect password");
        }
        if (!user.getActive()) {
            throw new ActionForbiddenException("User with email " + request.getEmail() + " is blocked");
        }
        return new LoginResponse(user.getId(), securityService.generateToken(user));
    }
}
