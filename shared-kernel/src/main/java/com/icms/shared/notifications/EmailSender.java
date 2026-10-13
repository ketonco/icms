package com.icms.shared.notifications;
/**
 * Port for sending emails. Implementations live in the owning service.
 */
public interface EmailSender {
    /**
     * Sends an email message using the provided email message details.
     * @param emailMessage the email message to be sent
     */
    void send(EmailMessage emailMessage);
}
