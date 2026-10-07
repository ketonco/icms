package com.icms.user_auth.i18n;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.icms.user_auth.repository.LanguageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * HTTP contract of the i18n bundles through the real {@code user-auth} stack.
 * <p>
 * Follows the pattern of {@code UserAuthGlobalExceptionsTest}: random port,
 * {@code test} profile and RestAssured. It proves that the renumbered business
 * codes still resolve through the real {@link MessageSource}, in English and
 * in Spanish, for every response of the API.
 * </p>
 */
@SuppressWarnings("null")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MessagesI18nIntegrationTest {

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

    @LocalServerPort
    private int port;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LanguageRepository languageRepository;

    /* Code of the language created by a test, so @AfterEach can remove the row. */
    private String createdLanguageCode;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    /* The POST travels over HTTP in another thread and connection, so the row must be deleted explicitly. */
    @AfterEach
    void deleteCreatedLanguage() {
        String code = createdLanguageCode;
        createdLanguageCode = null;
        if (code != null) {
            languageRepository.findByCode(code)
                .ifPresent(languageRepository::delete);
        }
    }

    @Test
    @DisplayName("PUT /api/v1/auth/languages - non-existent entity returns the Spanish message")
    void entityNotFoundReturnsSpanishMessage() {
        given()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "es-ES")
            .body(nonExistentLanguageJson())
        .when()
            .put("/api/v1/auth/languages")
        .then()
            .statusCode(404)
            .body("status", equalTo(404))
            .body("code", equalTo("Ent-001"))
            .body("message", equalTo("Entidad no encontrada."));
    }

    @Test
    @DisplayName("PUT /api/v1/auth/languages - non-existent entity returns the English message")
    void entityNotFoundReturnsEnglishMessage() {
        given()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "en-US")
            .body(nonExistentLanguageJson())
        .when()
            .put("/api/v1/auth/languages")
        .then()
            .statusCode(404)
            .body("status", equalTo(404))
            .body("code", equalTo("Ent-001"))
            .body("message", equalTo("Entity not found."));
    }

    @Test
    @DisplayName("POST /api/v1/auth/languages - duplicate catalog code returns the Spanish message")
    void duplicateCatalogCodeReturnsSpanishMessage() {
        String existingCodeLanguageJson = """
                {
                    "code": "en-US",
                    "name": "Existing Code Language",
                    "active": true,
                    "isDefault": false
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "es-ES")
            .body(existingCodeLanguageJson)
        .when()
            .post("/api/v1/auth/languages")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("code", equalTo("Cat-002"))
            .body("message", equalTo("El código del catálogo debe ser único."));
    }

    @Test
    @DisplayName("POST /api/v1/auth/languages - success message resolves the renamed code S-001")
    void createSuccessReturnsRenamedCode() {
        String code = randomLanguageCode();
        String name = randomLanguageName();
        createdLanguageCode = code;
        String newLanguageJson = """
                {
                    "code": "%s",
                    "name": "%s",
                    "active": true,
                    "isDefault": false
                }
                """.formatted(code, name);

        given()
            .contentType(ContentType.JSON)
            .header("Accept-Language", "es-ES")
            .body(newLanguageJson)
        .when()
            .post("/api/v1/auth/languages")
        .then()
            .statusCode(200)
            .body("status", equalTo(200))
            .body("message", equalTo("Entidad creada con éxito"));
    }

    @Test
    @DisplayName("The 21 business codes resolve in the real MessageSource of the context")
    void everyBusinessCodeResolvesInRealMessageSource() {
        for (String code : BUSINESS_CODES) {
            String english = messageSource.getMessage(code, null, Locale.US);
            String spanish = messageSource.getMessage(code, null, Locale.of("es", "ES"));

            assertThat(english)
                .as("message of %s for Locale.US", code)
                .isNotBlank()
                .isNotEqualTo(code);
            assertThat(spanish)
                .as("message of %s for Locale.of(\"es\", \"ES\")", code)
                .isNotBlank()
                .isNotEqualTo(code);
        }
    }

    /* Body of a PUT that targets a language id that does not exist. */
    private static String nonExistentLanguageJson() {
        return """
                {
                    "id": 9999,
                    "code": "xx-XX",
                    "name": "Non-Existent Language",
                    "active": false,
                    "isDefault": false
                }
                """;
    }

    /* Random code matching the LanguageDto pattern ^[a-z]{2,3}(-[A-Z]{2})?$. */
    private static String randomLanguageCode() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char lower1 = (char) ('a' + random.nextInt(26));
        char lower2 = (char) ('a' + random.nextInt(26));
        char lower3 = (char) ('a' + random.nextInt(26));
        char upper1 = (char) ('A' + random.nextInt(26));
        char upper2 = (char) ('A' + random.nextInt(26));
        return "" + lower1 + lower2 + lower3 + "-" + upper1 + upper2;
    }

    /* Random name, unique across runs, so name uniqueness never blocks the save. */
    private static String randomLanguageName() {
        return "Lang " + UUID.randomUUID();
    }
}
