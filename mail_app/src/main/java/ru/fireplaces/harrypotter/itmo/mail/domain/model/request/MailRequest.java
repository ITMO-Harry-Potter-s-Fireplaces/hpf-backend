package ru.fireplaces.harrypotter.itmo.mail.domain.model.request;

import lombok.Data;
import org.springframework.util.StringUtils;
import ru.fireplaces.harrypotter.itmo.utils.interfaces.model.request.RequestRequiredFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Email info POJO for requests.
 *
 * @author seniorkot
 */
@Data
public class MailRequest implements RequestRequiredFields {

    /**
     * Receiver address.
     */
    private String to;

    /**
     * Email subject.
     */
    private String subject;

    /**
     * Email text.
     */
    private String text;

    @Override
    public List<String> getBlankRequiredFields() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isEmpty(this.to)) {
            list.add("to");
        }
        if (StringUtils.isEmpty(this.subject)) {
            list.add("subject");
        }
        if (StringUtils.isEmpty(this.text)) {
            list.add("text");
        }
        return list;
    }
}
