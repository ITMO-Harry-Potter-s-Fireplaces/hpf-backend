package ru.fireplaces.harrypotter.itmo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.service.EmailService;

/**
 * Implementation of {@link EmailService} interface.
 *
 * @author seniorkot
 */
@Primary
@Service(value = EmailServiceImpl.SERVICE_VALUE)
public class EmailServiceImpl implements EmailService {

    public static final String SERVICE_VALUE = "EmailServiceImpl";

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hp.fireplaces@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
