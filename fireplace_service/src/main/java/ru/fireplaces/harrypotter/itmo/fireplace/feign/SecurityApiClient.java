package ru.fireplaces.harrypotter.itmo.fireplace.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/security/permissions/role")
    CodeMessage<String> checkPermissionsAllowed(@RequestBody UserRoleRequest request);

    @PostMapping("/security/permissions/token")
    CodeMessage<String> verifyToken(@RequestBody TokenVerificationRequest request);

}
