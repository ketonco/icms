package com.icms.shared.i18n;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Stream;

import com.icms.shared.Utils.MessageResolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Locale resolution contract of {@link MessageResolver} against the real bundle.
 * <p>
 * Builds the same {@link ResourceBundleMessageSource} as production
 * ({@code i18n/messages}, UTF-8, code as default message) and verifies that
 * every business code resolves to a real message, in English and in Spanish,
 * and that an unknown code falls back to the code itself.
 * </p>
 */
//@SuppressWarnings("null")
class MessageResolverLocaleTest {

    /** The 21 business codes defined by the i18n standard, in bundle order. */
    private static final String[] BUSINESS_CODES = {
        "E-001",
        "Res-001",
        "S-001",
        "S-002",
        "S-003",
        "Ent-001",
        "Ent-002",
        "Ent-003",
        "Ent-004",
        "Ent-005",
        "Cat-001",
        "Cat-002",
        "Lan-001",
        "Lan-002",
        "Lan-003",
        "Usr-001",
        "Usr-002",
        "Usr-003",
        "UsrProf-001",
        "UsrProf-002",
        "UsrProf-003"
    };

    @BeforeEach
    void setUp() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setUseCodeAsDefaultMessage(true);
        new MessageResolver(messageSource);
    }

    @AfterEach
    void tearDown() {
        LocaleContextHolder.resetLocaleContext();
    }

    @Test
    @DisplayName("Ent-001 resolves to the English message for Locale.US")
    void resolvesEnglishForUsLocale() {
        LocaleContextHolder.setLocale(Locale.US);

        assertThat(MessageResolver.resolveMessage("Ent-001")).isEqualTo("Entity not found.");
    }

    @Test
    @DisplayName("Ent-001 resolves to the Spanish message for Locale.of(\"es\", \"ES\")")
    void resolvesSpanishForEsLocale() {
        LocaleContextHolder.setLocale(Locale.of("es", "ES"));

        assertThat(MessageResolver.resolveMessage("Ent-001")).isEqualTo("Entidad no encontrada.");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("businessCodes")
    @DisplayName("Every business code resolves to a message different from the code itself")
    void resolvesEveryBusinessCodeInBothLocales(String code) {
        LocaleContextHolder.setLocale(Locale.US);
        String english = MessageResolver.resolveMessage(code);
        assertThat(english)
            .as("code %s resolved for Locale.US", code)
            .isNotBlank()
            .isNotEqualTo(code);

        LocaleContextHolder.setLocale(Locale.of("es", "ES"));
        String spanish = MessageResolver.resolveMessage(code);
        assertThat(spanish)
            .as("code %s resolved for Locale.of(\"es\", \"ES\")", code)
            .isNotBlank()
            .isNotEqualTo(code);
    }

    @Test
    @DisplayName("An unknown code falls back to the code itself")
    void unknownCodeFallsBackToTheCode() {
        LocaleContextHolder.setLocale(Locale.US);

        assertThat(MessageResolver.resolveMessage("Qqq-999")).isEqualTo("Qqq-999");
    }

    /* Source of the parametrized test: the 21 business codes of the standard. */
    static Stream<String> businessCodes() {
        return Stream.of(BUSINESS_CODES);
    }
}
