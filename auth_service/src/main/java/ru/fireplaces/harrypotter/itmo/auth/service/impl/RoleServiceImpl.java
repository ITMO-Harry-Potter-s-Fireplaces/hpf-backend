package ru.fireplaces.harrypotter.itmo.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.auth.domain.dao.RoleRepository;
import ru.fireplaces.harrypotter.itmo.auth.domain.dao.UserRepository;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.Role;
import ru.fireplaces.harrypotter.itmo.auth.domain.model.request.RoleRequest;
import ru.fireplaces.harrypotter.itmo.auth.service.RoleService;
import ru.fireplaces.harrypotter.itmo.utils.exception.ActionInapplicableException;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityAlreadyExistsException;
import ru.fireplaces.harrypotter.itmo.utils.exception.EntityNotFoundException;

import java.util.List;

/**
 * Implementation of {@link RoleService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = RoleServiceImpl.SERVICE_VALUE)
public class RoleServiceImpl implements RoleService {

    public static final String SERVICE_VALUE = "RoleServiceImpl";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Role> getRolesPage(@NonNull Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRole(@NonNull Long id) throws EntityNotFoundException {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Role.class, id));
    }

    @Override
    public Role addRole(@NonNull RoleRequest newRole) throws BadInputDataException, EntityAlreadyExistsException {
        List<String> blankFields = newRole.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(RoleRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        if (roleRepository.existsByName(newRole.getName())) {
            throw new EntityAlreadyExistsException("Role with such name already exists");
        }
        Role role = new Role();
        role.copy(newRole);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(@NonNull Long id, @NonNull RoleRequest newRole)
            throws BadInputDataException, EntityNotFoundException, EntityAlreadyExistsException {
        List<String> blankFields = newRole.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(RoleRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        Role role = getRole(id);
        if (!role.getName().equals(newRole.getName()) && roleRepository.existsByName(newRole.getName())) {
            throw new EntityAlreadyExistsException("Role with such name already exists");
        }
        role.copy(newRole);
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(@NonNull Long id) throws EntityNotFoundException {
        Role role = getRole(id);
        if (userRepository.countByRole(role) > 0) {
            throw new ActionInapplicableException("Role is used by one or many users");
        }
        roleRepository.delete(role);
    }
}
