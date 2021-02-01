package ru.fireplaces.harrypotter.itmo.service.impl;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.domain.model.User;
import ru.fireplaces.harrypotter.itmo.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.service.SecurityService;
import ru.fireplaces.harrypotter.itmo.service.UserService;
import ru.fireplaces.harrypotter.itmo.utils.Constants;
import ru.fireplaces.harrypotter.itmo.utils.enums.Role;
import ru.fireplaces.harrypotter.itmo.utils.exception.*;

import java.util.List;

/**
 * Implementation of {@link UserService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = UserServiceImpl.SERVICE_VALUE)
public class UserServiceImpl implements UserService {

    public static final String SERVICE_VALUE = "UserServiceImpl";

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Override
    public Page<User> getUsersPage(@NonNull Pageable pageable,
                                   @Nullable List<Role> roles,
                                   @Nullable Boolean active) {
        if (active != null) {
            if (roles != null) {
                return userRepository.findAllByActiveAndRoleIn(pageable, active, roles);
            }
            return userRepository.findAllByActive(pageable, active);
        }
        if (roles != null) {
            return userRepository.findAllByRoleIn(pageable, roles);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> getUsers(@Nullable List<Role> roles,
                               @Nullable Boolean active) {
        if (active != null) {
            if (roles != null) {
                return userRepository.findAllByActiveAndRoleIn(active, roles);
            }
            return userRepository.findAllByActive(active);
        }
        if (roles != null) {
            return userRepository.findAllByRoleIn(roles);
        }
        return userRepository.findAll();
    }

    @Override
    public User getUser(@NonNull Long id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

    }

    @Override
    public User updateUser(@NonNull Long id, @NonNull UserRequest userRequest, @NonNull Boolean copy)
            throws BadInputDataException, EntityNotFoundException, EntityAlreadyExistsException {
        if (copy) {
            List<String> blankFields = userRequest.getBlankRequiredFields();
            if (blankFields.size() > 0) {
                throw new BadInputDataException(UserRequest.class,
                        String.join(", ", blankFields), "are missing");
            }
        }
        User user = getUser(id);
        return update(userRequest, user, copy);
    }

    @Override
    public void deleteUser(@NonNull Long id) throws EntityNotFoundException {
        User user = getUser(id);
        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(0L) && currentUser.getRole().getValue() >= user.getRole().getValue()) {
            throw new ActionForbiddenException("Not enough permissions to change this active status");
        }
        user.setActive(!user.getActive());
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        return securityService.authorizeToken(MDC.get(Constants.KEY_MDC_AUTH_TOKEN));
    }

    @Override
    public User updateCurrentUser(@NonNull UserRequest userRequest, @NonNull Boolean copy)
            throws BadInputDataException, EntityAlreadyExistsException {
        if (copy) {
            List<String> blankFields = userRequest.getBlankRequiredFields();
            if (blankFields.size() > 0) {
                throw new BadInputDataException(UserRequest.class,
                        String.join(", ", blankFields), "are missing");
            }
        }
        User user = getCurrentUser();
        return update(userRequest, user, copy);
    }

    @Override
    public void deleteCurrentUser() {
        User user = getCurrentUser();
        userRepository.delete(user);
    }

    @Override
    public User changeUserRole(@NonNull Long id, @NonNull Role role)
            throws EntityNotFoundException, ActionForbiddenException {
        User currentUser = getCurrentUser();
        User user = getUser(id);
        if (currentUser.getId().equals(user.getId())) {
            throw new ActionInapplicableException("Not allowed to change your own role");
        }
        if (!currentUser.getId().equals(0L) && (role.equals(Role.ADMIN) || user.getRole().equals(Role.ADMIN))) {
            throw new ActionForbiddenException("Not enough permissions to assign or remove admins");
        }
        user.setRole(role);
        return userRepository.save(user);
    }

    /**
     * Private method to update user.
     *
     * @param userRequest User params
     * @param user User entity
     * @param copy Copy or ignore null fields
     * @return Updated {@link User} entity
     * @throws EntityAlreadyExistsException User with such email already exists
     */
    private User update(@NonNull UserRequest userRequest,
                        @NonNull User user,
                        @NonNull Boolean copy) throws EntityAlreadyExistsException {
        if (userRequest.getEmail() != null
                && !user.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EntityAlreadyExistsException("User with such email already exists");
        }
        if (copy) {
            user.copy(userRequest);
        }
        else {
            user.update(userRequest);
        }
        return userRepository.save(user);
    }
}
