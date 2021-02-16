package ru.fireplaces.harrypotter.itmo.mail.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fireplaces.harrypotter.itmo.mail.domain.model.request.MailRequest;
import ru.fireplaces.harrypotter.itmo.mail.service.MailService;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponse;
import ru.fireplaces.harrypotter.itmo.utils.response.CodeMessageResponseBuilder;

/**
 * REST controller for register and login endpoints.
 *
 * @author seniorkot
 */
@RestController
@RequestMapping(MailController.CONTROLLER_PATH)
public class MailController {

    /**
     * Controller path value.
     */
    public static final String CONTROLLER_PATH = "/mail";

    private static final Logger logger = LogManager.getLogger(MailController.class);

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Sends an email message.
     *
     * @param mailRequest Email information
     * @return <b>Response code</b>: 204<br>
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CodeMessageResponse<String> sendMail(@RequestBody MailRequest mailRequest) {
        logger.info("sendMail: mailRequest=" + mailRequest);
        mailService.sendEmail(mailRequest);
        CodeMessageResponse<String> response = CodeMessageResponseBuilder.noContent();
        logger.info("sendMail: response=" + response.getBody());
        return response;
    }
}
