package com.icms.api.userauth;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserAuthGatewayRoutingIT {

    @LocalServerPort 
    private int port;
    private static WireMockServer wireMockServer;
    private WebTestClient webTestClient;

    @BeforeAll
    static void startStub() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void stopStub() {
        wireMockServer.stop();
    }

    /*
        Point the gateway to the stub before the context starts (why: the URI is resolved when routes are created; 
        by @BeforeEach it is too late, and the dynamic port is not known in fixed annotations):
    */

    @DynamicPropertySource
    static void routeToStub(DynamicPropertyRegistry registry) {
        registry.add("HOST_USER_AUTH", () -> "localhost");
        registry.add("PORT_USER_AUTH", wireMockServer::port);
    }

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
        // Stub the User-Auth service endpoints

        // 1. Stub for /api/v1/auth/test endpoint through the API Gateway
        // 2. Stub for /api/v1/auth/languages endpoint through the API Gateway
        // 3. Stub for /api/v1/auth/languages/code/en-US endpoint through the API Gateway
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/auth/test"))
                .willReturn(ok("User-Auth Service is running")));
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/auth/languages"))
                .willReturn(okJson("{\"status\":200,\"message\":\"OK\",\"data\":[{\"code\":\"en-US\",\"name\":\"English\"}]}")));
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/auth/languages/code/en-US"))
                .willReturn(okJson("{\"status\":200,\"message\":\"OK\",\"data\":{\"code\":\"en-US\",\"name\":\"English\"}}")));
        
        // Initialize the WebTestClient to point to the WireMock server
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
