package ru.fireplaces.harrypotter.itmo.utils.domain.model.request;

import lombok.Data;
import org.springframework.util.StringUtils;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Token POJO. Used in request bodies to verify it.
 *
 * @author seniorkot
 */
@Data
public class TokenVerificationRequest implements RequestRequiredFields {

    /**
     * Authorization token.
     */
    private String token;

    /**
     * Parametrized constructor for token verification request.
     *
     * @param token Auth token
     */
    public TokenVerificationRequest(String token) {
        this.token = token;
    }

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(this.token)) {
            list.add("token");
        }
        return list;
    }
}
