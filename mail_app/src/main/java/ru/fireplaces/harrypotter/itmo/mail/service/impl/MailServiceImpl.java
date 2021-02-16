package ru.fireplaces.harrypotter.itmo.mail.service.impl;

import ru.fireplaces.harrypotter.itmo.mail.domain.model.request.MailRequest;
import ru.fireplaces.harrypotter.itmo.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.utils.exception.BadInputDataException;

import java.util.List;

/**
 * Implementation of {@link MailService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = MailServiceImpl.SERVICE_VALUE)
public class MailServiceImpl implements MailService {

    public static final String SERVICE_VALUE = "MailServiceImpl";

    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hp.fireplaces@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendEmail(MailRequest mailRequest) {
        List<String> blankFields = mailRequest.getBlankRequiredFields(); // Get blank fields
        if (blankFields.size() > 0) {
            throw new BadInputDataException(MailRequest.class,
                    String.join(", ", blankFields), "are missing");
        }
        sendEmail(mailRequest.getTo(), mailRequest.getSubject(), mailRequest.getText());
    }
}
