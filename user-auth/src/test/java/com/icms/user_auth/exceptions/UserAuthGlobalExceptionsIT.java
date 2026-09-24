package com.icms.user_auth.exceptions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.icms.shared.dto.RestResponse;
import com.icms.shared.entity.Language;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserAuthGlobalExceptionsIT {

    
    @LocalServerPort private int port;
    private WebTestClient webTestClient; 

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    /**
     * Integration tests to validate the update exception handling service
     * we update an entity that does not exist to trigger the exception.
     * This ensures that the global exception handler is properly invoked and the correct response is returned.
     */
    @Test 
    @DisplayName("PUT /api/v1/auth/languages - Test global exception handling for non-existent entity")
    void testGlobalExceptionHandlingForNonExistentEntity() {
        // we create the json object representing the non-existent language entity (id, code, name, active, isDefault)
        String nonExistentLanguageJson = """
                {
                    "id": 9999,
                    "code": "xx-XX",
                    "name": "Non-Existent Language",
                    "active": false,
                    "isDefault": false
                }
                """;

        webTestClient.put()
                .uri("/api/v1/auth/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistentLanguageJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(new ParameterizedTypeReference<RestResponse<Language>>() {})
                .consumeWith(response -> {
                    RestResponse<Language> body = response.getResponseBody();
                    assertThat(body).isNotNull();
                    assertThat(body.getStatus()).isEqualTo(404);
                    assertThat(body.getCode()).isEqualTo("Ent-001");
                    assertThat(body.getMessage()).isNotBlank();
                });
    }

    /**
    * Integration test to validate exception handling for existing code conflicts (e.g., duplicate entries).
     * We attempt to create an entity with a code that already exists to trigger the exception.
     * This ensures that the global exception handler is properly invoked and the correct response is returned.
     */
    @Test
    @DisplayName("POST /api/v1/auth/languages - Test global exception handling for existing code conflict")
    void testGlobalExceptionHandlingForExistingCodeConflict() {
        // we create the json object representing the language entity with an existing code
        String existingCodeLanguageJson = """
                {
                    "code": "en-US",
                    "name": "Existing Code Language",
                    "active": true,
                    "isDefault": false
                }
                """;

        webTestClient.post()
                .uri("/api/v1/auth/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCodeLanguageJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(new ParameterizedTypeReference<RestResponse<Language>>() {})
                .consumeWith(response -> {
                    RestResponse<Language> body = response.getResponseBody();
                    assertThat(body).isNotNull();
                    assertThat(body.getStatus()).isEqualTo(400);
                    assertThat(body.getCode()).isEqualTo("Cat-002");
                    assertThat(body.getMessage()).isNotBlank();
                });
    }

}
