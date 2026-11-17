package com.icms.user_auth.service.notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.icms.shared.exceptions.EmailSendException;
import com.icms.shared.notifications.EmailMessage;
import com.icms.shared.notifications.EmailSender;
import com.icms.user_auth.entity.EmailOutbox;
import com.icms.user_auth.repository.EmailOutboxRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor 
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource messageSource;
    private final EmailOutboxRepository emailOutboxRepository;

    /**
     * Sends an email message asynchronously.
     * This method creates an entry in the email outbox, attempts to send the email using the SMTP server,
     * @param emailMessage the email message to be sent
     */
    @Override
    @Async 
    public void send(EmailMessage message) {
        // Create a new email outbox entry with status "PENDING" before attempting to send the email
        // This ensures that there is a record of the email attempt even if sending fails
        EmailOutbox outbox = EmailOutbox.builder()
                .recipient(message.to())
                .subject(messageSource.getMessage("mail.confirm.subject", null, message.locale()))
                .template(message.template())
                .status("PENDING")
                .attempts(0)
                .build();
        outbox = emailOutboxRepository.save(outbox);
        // Attempt to send the email and update the outbox status accordingly
        try {
            // Prepare the email content using the Thymeleaf template engine
            Context ctx = new Context(message.locale() == null ? Locale.getDefault() : message.locale(),
                message.vars());
            String html = templateEngine.process(message.template(), ctx); // "confirm-account", sin extensión
            // Create a MIME message for the email using the JavaMailSender
            MimeMessage mime = mailSender.createMimeMessage();
            // Initialize the helper for the MIME message with UTF-8 encoding
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, StandardCharsets.UTF_8.name());
            // Set the recipient, subject, and HTML content for the email
            helper.setTo(message.to());
            helper.setSubject(outbox.getSubject());
            helper.setText(html, true);
            // Send the email using the JavaMailSender
            mailSender.send(mime);
            // Log the successful sending of the email
            outbox.setStatus("SENT");
            outbox.setSentAt(java.time.LocalDateTime.now());
            // Save the updated outbox entry after successfully sending the email
            emailOutboxRepository.save(outbox);
        } catch (Exception ex) {
            outbox.setStatus("FAILED");
            outbox.setAttempts(outbox.getAttempts() + 1);
            outbox.setLastError(ex.getMessage() == null ? ex.toString()
                : ex.getMessage().substring(0, Math.min(500, ex.getMessage().length())));
            emailOutboxRepository.save(outbox);
            throw new EmailSendException("Email-001", ex.getMessage());
        }
        //emailOutboxRepository.save(outbox);
    }

}