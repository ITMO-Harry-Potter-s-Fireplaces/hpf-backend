package ru.fireplaces.harrypotter.itmo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service interface for sending emails.
 *
 * @author seniorkot
 */
@Service
public interface EmailService {

    /**
     * Sends simple e-mail without attachments.
     *
     * @param to Receiver email address
     * @param subject Mail subject
     * @param text Mail text
     */
    void sendEmail(String to, String subject, String text);
}
