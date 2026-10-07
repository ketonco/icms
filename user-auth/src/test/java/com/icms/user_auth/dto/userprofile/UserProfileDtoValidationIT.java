package com.icms.user_auth.dto.userprofile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link UserProfileDto} Bean Validation messages i18n.
 * <p>
 * Verifies that validation messages are resolved through the configured
 * {@link org.springframework.context.MessageSource} based on the current
 * {@link Locale} set in {@link LocaleContextHolder}.
 * </p>
 */
@SuppressWarnings("null")
@SpringBootTest
@ActiveProfiles("test")
class UserProfileDtoValidationIT {

    @Autowired
    private Validator validator;

    /**
     * Helper method to validate a DTO and return the violations.
     *
     * @param dto the DTO to validate
     * @return set of constraint violations
     */
    private Set<ConstraintViolation<UserProfileDto>> validate(UserProfileDto dto) {
        return validator.validate(dto);
    }

    /**
     * Helper method to extract the violation message for a specific property and annotation type.
     *
     * @param violations the set of constraint violations
     * @param propertyPath the property path to search for (e.g., "firstName", "lastName", "email")
     * @param annotationType the annotation type to filter by (e.g., NotBlank.class, Size.class)
     * @return the violation message for the specified property and annotation, or empty string if not found
     */
    private String getMessageForPropertyAndAnnotation(Set<ConstraintViolation<UserProfileDto>> violations,
                                                      String propertyPath,
                                                      Class<? extends java.lang.annotation.Annotation> annotationType) {
        return violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals(propertyPath))
                .filter(v -> v.getConstraintDescriptor().getAnnotation().annotationType().equals(annotationType))
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("");
    }

    @Test
    @DisplayName("firstName blank with Accept-Language en-US should return English message")
    void firstNameBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserProfileDto dto = new UserProfileDto(null, "", "Doe", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "firstName", NotBlank.class))
                .isEqualTo("First name must not be blank");
    }

    @Test
    @DisplayName("firstName blank with Accept-Language es-ES should return Spanish message")
    void firstNameBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserProfileDto dto = new UserProfileDto(null, "", "Doe", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "firstName", NotBlank.class))
                .isEqualTo("El nombre no debe estar vacío");
    }

    @Test
    @DisplayName("firstName size with Accept-Language en-US should return English message")
    void firstNameSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserProfileDto dto = new UserProfileDto(null, "J", "Doe", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "firstName", Size.class))
                .isEqualTo("First name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("firstName size with Accept-Language es-ES should return Spanish message")
    void firstNameSizeSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserProfileDto dto = new UserProfileDto(null, "J", "Doe", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "firstName", Size.class))
                .isEqualTo("El nombre debe tener entre 2 y 50 caracteres");
    }

    @Test
    @DisplayName("lastName blank with Accept-Language en-US should return English message")
    void lastNameBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserProfileDto dto = new UserProfileDto(null, "John", "", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "lastName", NotBlank.class))
                .isEqualTo("Last name must not be blank");
    }

    @Test
    @DisplayName("lastName blank with Accept-Language es-ES should return Spanish message")
    void lastNameBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserProfileDto dto = new UserProfileDto(null, "John", "", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "lastName", NotBlank.class))
                .isEqualTo("El apellido no debe estar vacío");
    }

    @Test
    @DisplayName("lastName size with Accept-Language en-US should return English message")
    void lastNameSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserProfileDto dto = new UserProfileDto(null, "John", "D", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "lastName", Size.class))
                .isEqualTo("Last name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("lastName size with Accept-Language es-ES should return Spanish message")
    void lastNameSizeSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserProfileDto dto = new UserProfileDto(null, "John", "D", null, null, null, "john@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "lastName", Size.class))
                .isEqualTo("El apellido debe tener entre 2 y 50 caracteres");
    }

    @Test
    @DisplayName("email blank with Accept-Language en-US should return English message")
    void emailBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserProfileDto dto = new UserProfileDto(null, "John", "Doe", null, null, null, "");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", NotBlank.class))
                .isEqualTo("Email must not be blank");
    }

    @Test
    @DisplayName("email blank with Accept-Language es-ES should return Spanish message")
    void emailBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserProfileDto dto = new UserProfileDto(null, "John", "Doe", null, null, null, "");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", NotBlank.class))
                .isEqualTo("El correo no debe estar vacío");
    }

    @Test
    @DisplayName("email size with Accept-Language en-US should return English message")
    void emailSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserProfileDto dto = new UserProfileDto(null, "John", "Doe", null, null, null, "a".repeat(90) + "@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", Size.class))
                .isEqualTo("Email must be at most 100 characters");
    }

    @Test
    @DisplayName("email size with Accept-Language es-ES should return Spanish message")
    void emailSizeSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserProfileDto dto = new UserProfileDto(null, "John", "Doe", null, null, null, "a".repeat(90) + "@example.com");

        // Act
        Set<ConstraintViolation<UserProfileDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", Size.class))
                .isEqualTo("El correo debe tener como máximo 100 caracteres");
    }
}
