package com.icms.shared.notifications;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * EmailMessageTest
 * 1) AssertJ/Junit5 valid
 * 2) vars = null returns Map.of()
 * 3) locale = null returns default locale
 * 4) to = null returns IllegalArgumentException
 */
public class EmailMessageTest {

    // Test cases for EmailMessage class will be implemented here using AssertJ and JUnit5.
    @Test 
    @DisplayName("Test valid EmailMessage creation")
    public void testValidEmailMessageCreation() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String template = "Test Body";
        // Variables for the email message like placeholders for dynamic content
        Map<String, Object> vars = Map.of("date", "2024-06-06", "user", "testUser"); 
        Locale locale = Locale.getDefault();

        // Act
        EmailMessage emailMessage = new EmailMessage(to, subject, template, vars, locale);

        // Assert
        assertNotNull(emailMessage);
        assertEquals(to, emailMessage.to());
        assertEquals(subject, emailMessage.subject());
        assertEquals(template, emailMessage.template());
        assertEquals(vars, emailMessage.vars());
        assertEquals(locale, emailMessage.locale());
    }

    @Test
    @DisplayName("Test EmailMessage creation with null vars returns empty map")
    public void testEmailMessageCreationWithNullVars() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String template = "Test Body";
        Map<String, Object> vars = null;
        Locale locale = Locale.getDefault();

        // Act
        EmailMessage emailMessage = new EmailMessage(to, subject, template, vars, locale);

        // Assert
        assertNotNull(emailMessage);
        assertEquals(to, emailMessage.to());
        assertEquals(subject, emailMessage.subject());
        assertEquals(template, emailMessage.template());
        assertEquals(Map.of(), emailMessage.vars());
        assertEquals(locale, emailMessage.locale());
    }

    @Test 
    @DisplayName("Test EmailMessage creation with null locale returns default locale")
    public void testEmailMessageCreationWithNullLocale() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String template = "Test Body";
        Map<String, Object> vars = Map.of("date", "2024-06-06", "user", "testUser");
        Locale locale = null;

        // Act
        EmailMessage emailMessage = new EmailMessage(to, subject, template, vars, locale);

        // Assert
        assertNotNull(emailMessage);
        assertEquals(to, emailMessage.to());
        assertEquals(subject, emailMessage.subject());
        assertEquals(template, emailMessage.template());
        assertEquals(vars, emailMessage.vars());
        assertEquals(Locale.getDefault(), emailMessage.locale());
    }

    @Test
    @DisplayName("Test EmailMessage creation with null to address throws exception")
    public void testEmailMessageCreationWithNullTo() {
        // Arrange
        String to = null;
        String subject = "Test Subject";
        String template = "Test Body";
        Map<String, Object> vars = Map.of("date", "2024-06-06", "user", "testUser");
        Locale locale = Locale.getDefault();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new EmailMessage(to, subject, template, vars, locale);
        });
    }
}
