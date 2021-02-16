package ru.fireplaces.harrypotter.itmo.mail.service;

import org.springframework.stereotype.Service;
import ru.fireplaces.harrypotter.itmo.mail.domain.model.request.MailRequest;

/**
 * Service interface for sending emails.
 *
 * @author seniorkot
 */
@Service
public interface MailService {

    /**
     * Sends simple e-mail without attachments.
     *
     * @param to Receiver email address
     * @param subject Mail subject
     * @param text Mail text
     */
    void sendEmail(String to, String subject, String text);

    /**
     * Sends simple e-mail without attachments.
     *
     * @param mailRequest Email information
     */
    void sendEmail(MailRequest mailRequest);
}
