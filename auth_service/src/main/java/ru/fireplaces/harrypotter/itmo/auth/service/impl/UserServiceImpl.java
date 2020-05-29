package ru.fireplaces.harrypotter.itmo.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.User;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.UserRequest;
import ru.fireplaces.harrypotter.itmo.auth.service.RoleService;
import ru.fireplaces.harrypotter.itmo.auth.service.UserService;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityAlreadyExistsException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

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
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public Page<User> getUsersPage(@NonNull Pageable pageable, List<Long> roleIds) {
        if (roleIds != null) {
            return userRepository.findAllByActiveAndRoleIn(pageable, true, roleIds);
        }
        return userRepository.findAllByActive(pageable, true);
    }

    @Override
    public List<User> getUsers(List<Long> roleIds) {
        if (roleIds != null) {
            return userRepository.findAllByActiveAndRoleIn(true, roleIds);
        }
        return userRepository.findAllByActive(true);
    }

    @Override
    public User getUser(@NonNull Long id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

    }

    @Override
    public User updateUser(@NonNull Long id, @NonNull UserRequest newUser)
            throws BadInputDataException, EntityNotFoundException, EntityAlreadyExistsException {
        List<String> blankFields = newUser.getBlankRequiredFields();
        if (blankFields.size() > 0) {
            throw new BadInputDataException(UserRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        User user = getUser(id);
        if (!user.getEmail().equals(newUser.getEmail()) && userRepository.existsByEmail(newUser.getEmail())) {
            throw new EntityAlreadyExistsException("User with such email already exists");
        }
        if (newUser.getRoleId() != null) {
            newUser.setRole(roleService.getRole(newUser.getRoleId()));
        }
        user.copy(newUser);
        return userRepository.save(user);
    }
}
