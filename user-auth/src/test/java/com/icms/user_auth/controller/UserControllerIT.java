package com.icms.user_auth.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.icms.user_auth.repository.UserRepository;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIT {
    
    @LocalServerPort private int port;

    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @AfterEach
    @Transactional
    void tearDown() {
        userRepository.deleteProfileByUsername("testuser2");
        userRepository.deleteUserTypesByUsername("testuser2");
        userRepository.deleteByUsername("testuser2");
    }

    @Test 
    @Transactional
    @DisplayName("POST /api/v1/auth/user - test creating a new user")
    void testCreateUser() {
        // Implement the test using RestAssured here
        // json payload for creating a new user with CreateUserDto fields
        String jsonPayload = """
        {
            "username": "testuser2",
            "password": "Testpassword.123456",
            "email": "testuser2@example.com",
            "profile": {
                "firstName": "Test",
                "lastName": "User",
                "avatarUrl": "http://example.com/avatar.jpg",
                "prefs": null,
                "contact": null,
                "email": "testuser2@example.com"
            }
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(jsonPayload)
        .when()
            .post("/api/v1/auth/user")
        .then()
            // TODO (pending P-23)
            .statusCode(200)
            .body("data.username", equalTo("testuser2"))
            .body("data.email", equalTo("testuser2@example.com")); 
    }

    // TODO (pending P-27)
}
