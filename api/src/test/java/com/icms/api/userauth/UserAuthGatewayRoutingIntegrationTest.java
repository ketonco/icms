package com.icms.api.userauth;
import static org.assertj.core.api.Assertions.assertThat;
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
    private int port;private WebTestClient webTestClient;

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
        // NOTA: Para que este test pase, el servicio 'user-auth' debe estar corriendo en localhost:8081
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

}
