package com.icms.user_auth.dto.usertype;

import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;
import java.util.Set;

import jakarta.validation.ConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link UserTypeDto} Bean Validation messages i18n.
 * <p>
 * Verifies that validation messages are resolved through the configured
 * {@link org.springframework.context.MessageSource} based on the current
 * {@link Locale} set in {@link LocaleContextHolder}.
 * </p>
 */
@SuppressWarnings("null")
@SpringBootTest
@ActiveProfiles("test")
class UserTypeDtoValidationIT {

    @Autowired
    private Validator validator;

    /**
     * Helper method to validate a DTO and return the violation messages.
     *
     * @param dto the DTO to validate
     * @return set of violation messages
     */
    private Set<ConstraintViolation<UserTypeDto>> validate(UserTypeDto dto) {
        return validator.validate(dto);
    }

    /**
     * Helper method to extract the violation message for a specific property and annotation type.
     *
     * @param violations the set of constraint violations
     * @param propertyPath the property path to search for (e.g., "code", "name", "active")
     * @param annotationType the annotation type to filter by (e.g., NotBlank.class, Pattern.class)
     * @return the violation message for the specified property and annotation, or empty string if not found
     */
    private String getMessageForPropertyAndAnnotation(Set<ConstraintViolation<UserTypeDto>> violations,
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
    @DisplayName("code blank with Accept-Language en-US should return English message")
    void codeBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserTypeDto dto = new UserTypeDto(null, "", "Admin", true);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", NotBlank.class))
                .isEqualTo("Code must not be blank");
    }

    @Test
    @DisplayName("code blank with Accept-Language es-ES should return Spanish message")
    void codeBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserTypeDto dto = new UserTypeDto(null, "", "Admin", true);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", NotBlank.class))
                .isEqualTo("El código no debe estar vacío");
    }

    @Test
    @DisplayName("code pattern with Accept-Language en-US should return English message")
    void codePatternEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserTypeDto dto = new UserTypeDto(null, "ab", "Admin", true);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", Pattern.class))
                .isEqualTo("Code must be 3 uppercase letters");
    }

    @Test
    @DisplayName("code pattern with Accept-Language es-ES should return Spanish message")
    void codePatternSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserTypeDto dto = new UserTypeDto(null, "ab", "Admin", true);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", Pattern.class))
                .isEqualTo("El código debe tener 3 letras mayúsculas");
    }

    @Test
    @DisplayName("name blank with Accept-Language en-US should return English message")
    void nameBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserTypeDto dto = new UserTypeDto(null, "ADM", "", true);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "name", NotBlank.class))
                .isEqualTo("Name must not be blank");
    }

    @Test
    @DisplayName("name blank with Accept-Language es-ES should return Spanish message")
    void nameBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserTypeDto dto = new UserTypeDto(null, "ADM", "", true);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "name", NotBlank.class))
                .isEqualTo("El nombre no debe estar vacío");
    }

    @Test
    @DisplayName("active null with Accept-Language en-US should return English message")
    void activeNullEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserTypeDto dto = new UserTypeDto(null, "ADM", "Admin", null);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "active", NotNull.class))
                .isEqualTo("Active cannot be null");
    }

    @Test
    @DisplayName("active null with Accept-Language es-ES should return Spanish message")
    void activeNullSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserTypeDto dto = new UserTypeDto(null, "ADM", "Admin", null);

        // Act
        Set<ConstraintViolation<UserTypeDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "active", NotNull.class))
                .isEqualTo("Activo no puede ser nulo");
    }
}
