package ru.fireplaces.harrypotter.itmo.fireplace.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.request.LoginRequest;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.response.LoginResponse;
import ru.fireplaces.harrypotter.itmo.fireplace.domain.model.response.User;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.TokenVerificationRequest;
import ru.fireplaces.harrypotter.itmo.utils.domain.model.request.UserRoleRequest;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessage;

/**
 * Feign client to communicate with Security API service.
 *
 * @author seniorkot
 */
@FeignClient("security")
public interface SecurityApiClient {

    @PostMapping("/auth/login")
    CodeMessage<LoginResponse> login(@RequestBody LoginRequest request);

    @PostMapping("/permissions/role")
    CodeMessage<String> checkPermissionsAllowed(@RequestBody UserRoleRequest request);

    @PostMapping("/permissions/token")
    CodeMessage<String> verifyToken(@RequestBody TokenVerificationRequest request);

    @GetMapping("/users/{id}")
    CodeMessage<User> getUser(@RequestHeader("Authorization") String token,
                              @PathVariable Long id);

    @GetMapping("/users/current")
    CodeMessage<User> getCurrentUser(@RequestHeader("Authorization") String token);
}
