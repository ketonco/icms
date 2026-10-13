package com.icms.shared.exceptions;

/**
 * Exception thrown when an email fails to send.
 * This exception is typically used to indicate that an attempt to send an email has failed.
 */
public class EmailSendException extends BaseException{
    /**
     * Constructs a new EmailSendException with the specified error code.
     * @param code the error code representing the specific email send failure
     */
    public EmailSendException(String code) {
        super(code);
    }

    /**
     * Constructs a new EmailSendException with the specified error code and message.
     * @param code the error code representing the specific email send failure
     * @param message the detailed message explaining the email send failure
     */
    public EmailSendException(String code, String message) {
        super(code, message);
    }
}
