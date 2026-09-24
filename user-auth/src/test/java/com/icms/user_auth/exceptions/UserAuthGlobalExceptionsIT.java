package com.icms.user_auth.exceptions;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserAuthGlobalExceptionsIT {

    
    @LocalServerPort private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
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

        given()
            .contentType(ContentType.JSON)
            .body(nonExistentLanguageJson)
        .when()
            .put("/api/v1/auth/languages")
        .then()
            .statusCode(404)
            .body("status", equalTo(404))
            .body("code", equalTo("Ent-001"))
            .body("message", not(emptyOrNullString()));
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

        given()
            .contentType(ContentType.JSON)
            .body(existingCodeLanguageJson)
        .when()
            .post("/api/v1/auth/languages")
        .then()
            .statusCode(400)
            .body("status", equalTo(400))
            .body("code", equalTo("Cat-002"))
            .body("message", not(emptyOrNullString()));
    }

}
