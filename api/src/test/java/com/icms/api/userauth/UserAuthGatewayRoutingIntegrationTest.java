package com.icms.api.userauth;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserAuthGatewayRoutingIntegrationTest {

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
     * integration test for the /api/v1/auth/test endpoint through the API Gateway.
     * Verifies that the gateway correctly routes the request to the User-Auth service.
    */
    @Test
    @DisplayName("GET /api/v1/auth/test - Gateway debe redirigir la petición a User-Auth")
    void testGatewayRoutingToUserAuth() {
        // NOTE: In order for this test to pass, the 'user-auth' service must be running on localhost:8081
        webTestClient.get()
                .uri("/api/v1/auth/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertThat(body).isEqualTo("User-Auth Service is running");
                });
    }

    /**
     * integration test for the /api/v1/auth/languages endpoint through the API Gateway.
     * Verifies that the gateway correctly routes the request to the User-Auth service.
     */
    @Test
    @DisplayName("GET /api/v1/auth/languages - Gateway debe redirigir la petición a User-Auth")
    void testGatewayRoutingToUserAuthLanguages() {
        // NOTE: In order for this test to pass, the 'user-auth' service must be running on localhost:8081
        webTestClient.get()
                .uri("/api/v1/auth/languages")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    // convert body to json to extract specific fields (RestResponse)
                    try {
                        JsonNode jsonNode = new ObjectMapper().readTree(body);
                        assertThat(jsonNode.get("status").asInt()).isEqualTo(200);
                        assertThat(jsonNode.get("message").asText()).isEqualTo("OK");
                        assertThat(jsonNode.get("data").isArray()).isTrue();
                    } catch (JsonProcessingException exception) {
                        throw new AssertionError("Response body is not valid JSON", exception);
                    }
                });
    }

    /**
     * integration test for the /api/v1/auth/languages/code/{code} endpoint through the API Gateway.
     * Verifies that the gateway correctly routes the request to the User-Auth service.
     */
    @Test
    @DisplayName("GET /api/v1/auth/languages/code/{code} - Gateway debe redirigir la petición a User-Auth")
    void testGatewayRoutingToUserAuthLanguageByCode() {
        // NOTE: In order for this test to pass, the 'user-auth' service must be running on localhost:8081
        webTestClient.get()
                .uri("/api/v1/auth/languages/code/en-US")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    // convert body to json to extract specific fields (RestResponse)
                    try {
                        JsonNode jsonNode = new ObjectMapper().readTree(body);
                        assertThat(jsonNode.get("status").asInt()).isEqualTo(200);
                        assertThat(jsonNode.get("message").asText()).isEqualTo("OK");
                        assertThat(jsonNode.get("data").isObject()).isTrue();
                        assertThat(jsonNode.get("data").get("code").asText()).isEqualTo("en-US");
                        assertThat(jsonNode.get("data").get("name").asText()).isEqualTo("English");
                    } catch (JsonProcessingException exception) {
                        throw new AssertionError("Response body is not valid JSON", exception);
                    }
                });
    }

}
