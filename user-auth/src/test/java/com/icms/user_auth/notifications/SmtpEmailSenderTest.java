package com.icms.user_auth.notifications;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.context.MessageSource;

import com.icms.shared.exceptions.EmailSendException;
import com.icms.shared.notifications.EmailMessage;
import com.icms.user_auth.entity.EmailOutbox;
import com.icms.user_auth.repository.EmailOutboxRepository;
import com.icms.user_auth.service.notification.SmtpEmailSender;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;

@ExtendWith(MockitoExtension.class)
public class SmtpEmailSenderTest {

    static final String RECIPIENT = "mbarroeta1990@gmail.com";
    static final String TEMPLATE = "confirm-account";
    static final String RENDERED_HTML = "<html><body>Confirm link: http://localhost:8080/confirm?token=abc</body></html>";

    private EmailMessage emailMessage;

    @Mock
    JavaMailSender mailSender;
    @Mock
    SpringTemplateEngine templateEngine;
    @Mock
    MessageSource messageSource;
    @Mock
    EmailOutboxRepository emailOutboxRepository;
    @InjectMocks 
    SmtpEmailSender smtpEmailSender;

    @BeforeEach
    public void setUp() {
        emailMessage = new EmailMessage(RECIPIENT,
            RECIPIENT,
            TEMPLATE,
            Map.of("name", "Maximo Barroeta", "link", "http://localhost:8080/api/v1/auth/user/confirm", "expiresHours", 24), 
            Locale.ENGLISH);
            
        // Return the same instance so the captor observes the state transitions
        when(emailOutboxRepository.save(any(EmailOutbox.class)))
            .thenAnswer(invocation -> {
                EmailOutbox toSave = invocation.getArgument(0);
                EmailOutbox saved = new EmailOutbox();
                saved.setRecipient(toSave.getRecipient()); 
                saved.setSubject(toSave.getSubject());
                saved.setStatus(toSave.getStatus());
                saved.setAttempts(toSave.getAttempts());
                saved.setLastError(toSave.getLastError());
                saved.setSentAt(toSave.getSentAt());
                return saved;
            });   
    }

    @Test
    @DisplayName ("Test status pending and sent at the moment when sending an email")
    public void sendSuccessfulMarksStatus() throws Exception {
        
        when(messageSource.getMessage("mail.confirm.subject", null, emailMessage.locale()))
            .thenReturn("Confirm your ICMS account");

        when(templateEngine.process(eq(TEMPLATE), any(Context.class)))
        .thenReturn(RENDERED_HTML);

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        smtpEmailSender.send(emailMessage);

        // Note: Capturing MimeMessageHelper is tricky because it's created inside the method and not injected.
        // Instead, we can verify the behavior on the MimeMessage itself.
        verify(mailSender).send(mimeMessage);

        // Capture the EmailOutbox entity that was saved to the repository
        ArgumentCaptor<EmailOutbox> outboxCaptor = ArgumentCaptor.forClass(EmailOutbox.class);
        // capture all the invocations as a list, use getAllValues() to get all captured values, getValue() to get the last captured value
        verify(emailOutboxRepository, Mockito.atLeast(2)).save(outboxCaptor.capture());

        EmailOutbox first = outboxCaptor.getAllValues().get(0); //PENDING status
        EmailOutbox last = outboxCaptor.getValue(); //SENT status

        assertThat(first.getStatus()).isEqualTo("PENDING");
        assertThat(last.getStatus()).isEqualTo("SENT");
        assertThat(last.getRecipient()).isEqualTo(RECIPIENT);
        assertThat(last.getSubject()).isEqualTo("Confirm your ICMS account");
        assertThat(last.getSentAt()).isNotNull();
        assertThat(last.getAttempts()).isZero();

    }

    @Test 
    @DisplayName("Test status failed when sending an email fails")
    public void sendFailureMarksStatus() throws Exception {
        when(messageSource.getMessage("mail.confirm.subject", null, emailMessage.locale()))
            .thenReturn("Confirm your ICMS account");

        when(templateEngine.process(eq(TEMPLATE), any(Context.class)))
            .thenReturn(RENDERED_HTML);

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Simulate an exception when sending the email
        doThrow(new EmailSendException("SMTP server error")).when(mailSender).send(mimeMessage);

        assertThatThrownBy(() -> smtpEmailSender.send(emailMessage))
            .isInstanceOf(EmailSendException.class);

        // Capture the EmailOutbox entity that was saved to the repository
        ArgumentCaptor<EmailOutbox> outboxCaptor = ArgumentCaptor.forClass(EmailOutbox.class);
        verify(emailOutboxRepository, Mockito.times(2)).save(outboxCaptor.capture());

        EmailOutbox last = outboxCaptor.getValue(); //FAILED status

        assertThat(last.getStatus()).isEqualTo("FAILED");
        assertThat(last.getRecipient()).isEqualTo(RECIPIENT);
        assertThat(last.getSubject()).isEqualTo("Confirm your ICMS account");
        assertThat(last.getAttempts()).isEqualTo(1);
        assertThat(last.getLastError()).contains("SMTP server error");
    }

}
