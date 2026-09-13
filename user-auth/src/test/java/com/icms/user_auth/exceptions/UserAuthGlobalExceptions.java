package com.icms.user_auth.exceptions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserAuthGlobalExceptions {

    @LocalServerPort 
    private int port;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    /**
     * Integration tests to validate the update service
     * we create an entity that does not exist to trigger the exception.
     * This ensures that the global exception handler is properly invoked and the correct response is returned.
     */
    @Test 
    @DisplayName("PUT /api/v1/auth/languages - Test global exception handling for non-existent entity")
    void testGlobalExceptionHandlingForNonExistentEntity() {
        // we create the json object representing the non-existent language entity (id, code, name, active, isDefault)
        String nonExistentLanguageJson = "{\"id\": 9999, " +
        "\"code\": \"xx-XX\", " +
        "\"name\": \"Non-Existent Language\", " +
        "\"active\": false, " +
        "\"isDefault\": false }";

        webTestClient.put()
                .uri("/api/v1/auth/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistentLanguageJson)
                .exchange()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    try {
                        JsonNode jsonNode = new ObjectMapper().readTree(body);
                        assertThat(jsonNode.get("status").asInt()).isEqualTo(404);
                        assertThat(jsonNode.get("code").asText()).isEqualTo("Ent-001");
                    } catch (Exception exception) {
                        throw new AssertionError("Unable to parse the error response", exception);
                    }
                });
    }

}
