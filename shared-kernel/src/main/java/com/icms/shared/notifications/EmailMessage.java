package com.icms.shared.notifications;
import java.util.Map;
import java.util.Locale;

/**
 * Represents an email message with recipient, subject, template, variables, and locale.
 * 
 * Contains validation to ensure that required fields are not null or blank, and provides default values for optional fields.
 * @param to the recipient email address
 * @param subject the subject of the email
 * @param template the template name for the email content
 * @param vars the variables to be used in the email template
 * @param locale the locale for the email content
 */
public record EmailMessage(
    String to,
    String subject,
    String template,
    Map<String, Object> vars,
    Locale locale
)
{
    public EmailMessage {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or blank");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject cannot be null or blank");
        }
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("Email template cannot be null or blank");
        }        
        vars = (vars == null) ? Map.of() : Map.copyOf(vars);
        locale = (locale == null) ? Locale.getDefault() : locale;
    }
}
