package ru.fireplaces.harrypotter.itmo.service.impl;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.dao.AuthLogRepository;
import ru.fireplaces.harrypotter.itmo.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.domain.model.AuthLog;
import ru.fireplaces.harrypotter.itmo.domain.model.User;
import ru.fireplaces.harrypotter.itmo.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.domain.model.response.LoginResponse;
import ru.fireplaces.harrypotter.itmo.service.AuthService;
import ru.fireplaces.harrypotter.itmo.service.EmailService;
import ru.fireplaces.harrypotter.itmo.service.SecurityService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.exception.*;

import javax.servlet.http.HttpServletRequest;
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
    private final AuthLogRepository authLogRepository;
    private final SecurityService securityService;
    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           AuthLogRepository authLogRepository,
                           SecurityService securityService,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.authLogRepository = authLogRepository;
        this.securityService = securityService;
        this.emailService = emailService;
    }

    @Override
    public LoginResponse register(@NonNull UserRequest userRequest,
                                  HttpServletRequest request)
            throws EntityAlreadyExistsException, BadInputDataException {
        List<String> blankFields = userRequest.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(LoginRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EntityAlreadyExistsException("User with email "
                    + userRequest.getEmail() + " already exists");
        }
        User user = new User();
        user.copy(userRequest);
        User savedUser = userRepository.save(user);
        authLogRepository.save(new AuthLog(savedUser, request.getRemoteAddr(), true));
        String emailMessage = "Дорогой, " + savedUser.getName() + "!\nМы рады приветствовать вас на нашем сервисе! " +
                "Благодарим вас за регистрацию и желаем вам приятных путешествий!\n\nС уважением,\nКоманда HPF";
        emailService.sendEmail(savedUser.getEmail(), "Регистрация на Harry Potter's Fireplaces", emailMessage);
        return new LoginResponse(savedUser.getId(), securityService.generateToken(savedUser));
    }

    @Override
    public LoginResponse login(@NonNull LoginRequest loginRequest,
                               HttpServletRequest request) throws EntityNotFoundException,
            BadInputDataException, UserUnauthorizedException, ActionForbiddenException {
        List<String> blankFields = loginRequest.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(LoginRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No User with email " + loginRequest.getEmail() + " found"));
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            authLogRepository.save(new AuthLog(user, request.getRemoteAddr(), false));
            throw new UserUnauthorizedException("Incorrect password");
        }
        if (!user.getActive()) {
            throw new ActionForbiddenException("User with email " + loginRequest.getEmail() + " is blocked");
        }
        authLogRepository.save(new AuthLog(user, request.getRemoteAddr(), true));
        return new LoginResponse(user.getId(), securityService.generateToken(user));
    }

    @Override
    public Page<AuthLog> getAuthHistory(@NonNull Pageable pageable,
                                        @NonNull Long id) throws EntityNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        return authLogRepository.findAllByUser(pageable, user);
    }

    @Override
    public Page<AuthLog> getCurrentAuthHistory(@NonNull Pageable pageable) {
        User user = securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
        return authLogRepository.findAllByUser(pageable, user);
    }
}
