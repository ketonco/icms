package com.icms.user_auth.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    /**
     * integration test for the /api/v1/auth/test endpoint.
     * Verifies that the service responds correctly when accessed directly.
    */
    @Test
    @DisplayName("GET /api/v1/auth/test - Debe responder directamente desde User-Auth")
    void testAuthHealthDirect() {
        RestAssured.get("/api/v1/auth/test")
                .then()
                .statusCode(200)
                .assertThat()
                .body(org.hamcrest.Matchers.equalTo("User-Auth Service is running"));
    }
}
