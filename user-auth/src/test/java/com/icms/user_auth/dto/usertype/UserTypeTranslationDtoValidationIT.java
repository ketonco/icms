package com.icms.user_auth.dto.usertype;

import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
 * Integration test for {@link UserTypeTranslationDto} Bean Validation messages i18n.
 * <p>
 * Verifies that validation messages are resolved through the configured
 * {@link org.springframework.context.MessageSource} based on the current
 * {@link Locale} set in {@link LocaleContextHolder}.
 * </p>
 */
@SuppressWarnings("null")
@SpringBootTest
@ActiveProfiles("test")
class UserTypeTranslationDtoValidationIT {

    @Autowired
    private Validator validator;

    /**
     * Helper method to validate a DTO and return the violation messages.
     *
     * @param dto the DTO to validate
     * @return set of violation messages
     */
    private Set<ConstraintViolation<UserTypeTranslationDto>> validate(UserTypeTranslationDto dto) {
        return validator.validate(dto);
    }

    /**
     * Helper method to extract the violation message for a specific property and annotation type.
     *
     * @param violations the set of constraint violations
     * @param propertyPath the property path to search for (e.g., "catalogId", "languageId", "translation")
     * @param annotationType the annotation type to filter by (e.g., NotNull.class, NotBlank.class)
     * @return the violation message for the specified property and annotation, or empty string if not found
     */
    private String getMessageForPropertyAndAnnotation(Set<ConstraintViolation<UserTypeTranslationDto>> violations,
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
    @DisplayName("catalogId null with Accept-Language en-US should return English message")
    void catalogIdNullEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserTypeTranslationDto dto = new UserTypeTranslationDto(null, null, 1L, "Translation", null);

        // Act
        Set<ConstraintViolation<UserTypeTranslationDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "catalogId", NotNull.class))
                .isEqualTo("Catalog ID cannot be blank");
    }

    @Test
    @DisplayName("catalogId null with Accept-Language es-ES should return Spanish message")
    void catalogIdNullSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserTypeTranslationDto dto = new UserTypeTranslationDto(null, null, 1L, "Translation", null);

        // Act
        Set<ConstraintViolation<UserTypeTranslationDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "catalogId", NotNull.class))
                .isEqualTo("El ID del catálogo no puede ser nulo");
    }

    @Test
    @DisplayName("languageId null with Accept-Language en-US should return English message")
    void languageIdNullEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserTypeTranslationDto dto = new UserTypeTranslationDto(null, 1L, null, "Translation", null);

        // Act
        Set<ConstraintViolation<UserTypeTranslationDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "languageId", NotNull.class))
                .isEqualTo("Language ID cannot be blank");
    }

    @Test
    @DisplayName("languageId null with Accept-Language es-ES should return Spanish message")
    void languageIdNullSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserTypeTranslationDto dto = new UserTypeTranslationDto(null, 1L, null, "Translation", null);

        // Act
        Set<ConstraintViolation<UserTypeTranslationDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "languageId", NotNull.class))
                .isEqualTo("El ID del idioma no puede ser nulo");
    }

    @Test
    @DisplayName("translation blank with Accept-Language en-US should return English message")
    void translationBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        UserTypeTranslationDto dto = new UserTypeTranslationDto(null, 1L, 1L, "", null);

        // Act
        Set<ConstraintViolation<UserTypeTranslationDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "translation", NotBlank.class))
                .isEqualTo("Translation cannot be blank");
    }

    @Test
    @DisplayName("translation blank with Accept-Language es-ES should return Spanish message")
    void translationBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        UserTypeTranslationDto dto = new UserTypeTranslationDto(null, 1L, 1L, "", null);

        // Act
        Set<ConstraintViolation<UserTypeTranslationDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "translation", NotBlank.class))
                .isEqualTo("La traducción no puede estar vacía");
    }
}
