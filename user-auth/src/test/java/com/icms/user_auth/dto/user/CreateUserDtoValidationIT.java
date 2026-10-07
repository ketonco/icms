package com.icms.user_auth.dto.user;

import com.icms.user_auth.dto.userprofile.UserProfileDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;
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
 * Integration test for {@link CreateUserDto} Bean Validation messages i18n.
 * <p>
 * Verifies that validation messages are resolved through the configured
 * {@link org.springframework.context.MessageSource} based on the current
 * {@link Locale} set in {@link LocaleContextHolder}.
 * </p>
 */
@SuppressWarnings("null")
@SpringBootTest
@ActiveProfiles("test")
class CreateUserDtoValidationIT {

    @Autowired
    private Validator validator;

    /**
     * Builds a valid user profile for tests that do not target the profile field.
     *
     * @return a valid {@link UserProfileDto}
     */
    private UserProfileDto validProfile() {
        return new UserProfileDto(null, "John", "Doe", null, null, null, "john@example.com");
    }

    /**
     * Helper method to validate a DTO and return the violations.
     *
     * @param dto the DTO to validate
     * @return set of constraint violations
     */
    private Set<ConstraintViolation<CreateUserDto>> validate(CreateUserDto dto) {
        return validator.validate(dto);
    }

    /**
     * Helper method to extract the violation message for a specific property and annotation type.
     *
     * @param violations the set of constraint violations
     * @param propertyPath the property path to search for (e.g., "username", "password")
     * @param annotationType the annotation type to filter by (e.g., NotBlank.class, Size.class)
     * @return the violation message for the specified property and annotation, or empty string if not found
     */
    private String getMessageForPropertyAndAnnotation(Set<ConstraintViolation<CreateUserDto>> violations,
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
    @DisplayName("username blank with Accept-Language en-US should return English message")
    void usernameBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("", "Passw0rd!", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "username", NotBlank.class))
                .isEqualTo("Username cannot be blank");
    }

    @Test
    @DisplayName("username blank with Accept-Language es-ES should return Spanish message")
    void usernameBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("", "Passw0rd!", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "username", NotBlank.class))
                .isEqualTo("El nombre de usuario no debe estar vacío");
    }

    @Test
    @DisplayName("username size with Accept-Language en-US should return English message")
    void usernameSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("ab", "Passw0rd!", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "username", Size.class))
                .isEqualTo("Username must be between 3 and 50 characters");
    }

    @Test
    @DisplayName("username size with Accept-Language es-ES should return Spanish message")
    void usernameSizeSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("ab", "Passw0rd!", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "username", Size.class))
                .isEqualTo("El nombre de usuario debe tener entre 3 y 50 caracteres");
    }

    @Test
    @DisplayName("password blank with Accept-Language en-US should return English message")
    void passwordBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("validUser", "", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "password", NotBlank.class))
                .isEqualTo("Password cannot be blank");
    }

    @Test
    @DisplayName("password blank with Accept-Language es-ES should return Spanish message")
    void passwordBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("validUser", "", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "password", NotBlank.class))
                .isEqualTo("La contraseña no debe estar vacía");
    }

    @Test
    @DisplayName("password size with Accept-Language en-US should return English message")
    void passwordSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("validUser", "Pw0!a", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "password", Size.class))
                .isEqualTo("Password must be between 6 and 100 characters");
    }

    @Test
    @DisplayName("password size with Accept-Language es-ES should return Spanish message")
    void passwordSizeSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("validUser", "Pw0!a", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "password", Size.class))
                .isEqualTo("La contraseña debe tener entre 6 y 100 caracteres");
    }

    @Test
    @DisplayName("password pattern with Accept-Language en-US should return English message")
    void passwordPatternEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("validUser", "password123!", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "password", Pattern.class))
                .isEqualTo("Password must include at least one number, one special character, one letter and one uppercase letter");
    }

    @Test
    @DisplayName("password pattern with Accept-Language es-ES should return Spanish message")
    void passwordPatternSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("validUser", "password123!", "user@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "password", Pattern.class))
                .isEqualTo("La contraseña debe incluir al menos un número, un carácter especial, una letra y una mayúscula");
    }

    @Test
    @DisplayName("email blank with Accept-Language en-US should return English message")
    void emailBlankEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", NotBlank.class))
                .isEqualTo("Email cannot be blank");
    }

    @Test
    @DisplayName("email blank with Accept-Language es-ES should return Spanish message")
    void emailBlankSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", NotBlank.class))
                .isEqualTo("El correo no debe estar vacío");
    }

    @Test
    @DisplayName("email format with Accept-Language en-US should return English message")
    void emailFormatEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "not-an-email", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", Email.class))
                .isEqualTo("Invalid email format");
    }

    @Test
    @DisplayName("email format with Accept-Language es-ES should return Spanish message")
    void emailFormatSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "not-an-email", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", Email.class))
                .isEqualTo("Formato de correo inválido");
    }

    @Test
    @DisplayName("email size with Accept-Language en-US should return English message")
    void emailSizeEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "a".repeat(90) + "@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

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
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "a".repeat(90) + "@example.com", validProfile());

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "email", Size.class))
                .isEqualTo("El correo debe tener como máximo 100 caracteres");
    }

    @Test
    @DisplayName("profile null with Accept-Language en-US should return English message")
    void profileNullEnglish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "user@example.com", null);

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "profile", NotNull.class))
                .isEqualTo("User profile cannot be null");
    }

    @Test
    @DisplayName("profile null with Accept-Language es-ES should return Spanish message")
    void profileNullSpanish() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        CreateUserDto dto = new CreateUserDto("validUser", "Passw0rd!", "user@example.com", null);

        // Act
        Set<ConstraintViolation<CreateUserDto>> violations = validate(dto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(getMessageForPropertyAndAnnotation(violations, "profile", NotNull.class))
                .isEqualTo("El perfil de usuario no puede ser nulo");
    }
}
