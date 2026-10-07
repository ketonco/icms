package com.icms.user_auth.dto.language;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
 * Integration test for {@link LanguageDto} Bean Validation messages i18n.
 * <p>
 * Verifies that validation messages are resolved through the configured
 * {@link org.springframework.context.MessageSource} based on the current
 * {@link Locale} set in {@link LocaleContextHolder}.
 * </p>
 */
@SuppressWarnings("null")
@SpringBootTest
@ActiveProfiles("test")
class LanguageDtoValidationIT {

    @Autowired
    private Validator validator;

    /**
     * Helper method to validate a DTO and return the violations.
     *
     * @param dto the DTO to validate
     * @return set of constraint violations
     */
    private Set<ConstraintViolation<LanguageDto>> validate(LanguageDto dto) {
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
    private String getMessageForPropertyAndAnnotation(Set<ConstraintViolation<LanguageDto>> violations,
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
        LanguageDto dto = new LanguageDto(null, "", "Spanish", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", NotBlank.class))
                .isEqualTo("Code cannot be blank");
    }

    @Test
    @DisplayName("code blank with Accept-Language es-ES should return Spanish message")
    void codeBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        LanguageDto dto = new LanguageDto(null, "", "Spanish", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", NotBlank.class))
                .isEqualTo("El código no debe estar vacío");
    }

    @Test
    @DisplayName("code size with Accept-Language en-US should return English message")
    void codeSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        LanguageDto dto = new LanguageDto(null, "a", "Spanish", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", Size.class))
                .isEqualTo("Code must be between 2 and 10 characters");
    }

    @Test
    @DisplayName("code size with Accept-Language es-ES should return Spanish message")
    void codeSizeSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        LanguageDto dto = new LanguageDto(null, "a", "Spanish", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", Size.class))
                .isEqualTo("El código debe tener entre 2 y 10 caracteres");
    }

    @Test
    @DisplayName("code pattern with Accept-Language en-US should return English message")
    void codePatternEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        LanguageDto dto = new LanguageDto(null, "XX", "Spanish", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", Pattern.class))
                .isEqualTo("Code must follow BCP 47 format (e.g. 'es', 'es-ES')");
    }

    @Test
    @DisplayName("code pattern with Accept-Language es-ES should return Spanish message")
    void codePatternSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        LanguageDto dto = new LanguageDto(null, "XX", "Spanish", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "code", Pattern.class))
                .isEqualTo("El código debe seguir el formato BCP 47 (ej. 'es', 'es-ES')");
    }

    @Test
    @DisplayName("name blank with Accept-Language en-US should return English message")
    void nameBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        LanguageDto dto = new LanguageDto(null, "es", "", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "name", NotBlank.class))
                .isEqualTo("Name cannot be blank");
    }

    @Test
    @DisplayName("name blank with Accept-Language es-ES should return Spanish message")
    void nameBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        LanguageDto dto = new LanguageDto(null, "es", "", false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "name", NotBlank.class))
                .isEqualTo("El nombre no debe estar vacío");
    }

    @Test
    @DisplayName("name size with Accept-Language en-US should return English message")
    void nameSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        LanguageDto dto = new LanguageDto(null, "es", "n".repeat(101), false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "name", Size.class))
                .isEqualTo("Name must be between 1 and 100 characters");
    }

    @Test
    @DisplayName("name size with Accept-Language es-ES should return Spanish message")
    void nameSizeSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        LanguageDto dto = new LanguageDto(null, "es", "n".repeat(101), false, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "name", Size.class))
                .isEqualTo("El nombre debe tener entre 1 y 100 caracteres");
    }

    @Test
    @DisplayName("isDefault null with Accept-Language en-US should return English message")
    void isDefaultNullEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        LanguageDto dto = new LanguageDto(null, "es", "Spanish", null, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "isDefault", NotNull.class))
                .isEqualTo("isDefault cannot be null");
    }

    @Test
    @DisplayName("isDefault null with Accept-Language es-ES should return Spanish message")
    void isDefaultNullSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        LanguageDto dto = new LanguageDto(null, "es", "Spanish", null, true);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "isDefault", NotNull.class))
                .isEqualTo("isDefault no puede ser nulo");
    }

    @Test
    @DisplayName("active null with Accept-Language en-US should return English message")
    void activeNullEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        LanguageDto dto = new LanguageDto(null, "es", "Spanish", false, null);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

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
        LanguageDto dto = new LanguageDto(null, "es", "Spanish", false, null);

        // Act
        Set<ConstraintViolation<LanguageDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "active", NotNull.class))
                .isEqualTo("Activo no puede ser nulo");
    }
}
