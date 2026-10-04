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
public class UserControllerTest {
    
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
        userRepository.deleteProfileByUsername("testuser");
        userRepository.deleteUserTypesByUsername("testuser");
        userRepository.deleteByUsername("testuser");
    }

    @Test 
    @Transactional
    @DisplayName("POST /api/v1/auth/user - test creating a new user")
    void testCreateUser() {
        // Implement the test using RestAssured here
        // json payload for creating a new user with CreateUserDto fields
        String jsonPayload = """
        {
            "username": "testuser",
            "password": "testpassword",
            "email": "testuser@example.com",
            "profile": {
                "firstName": "Test",
                "lastName": "User",
                "avatarUrl": "http://example.com/avatar.jpg",
                "prefs": null,
                "contact": null,
                "email": "testuser@example.com"
            }
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(jsonPayload)
        .when()
            .post("/api/v1/auth/user")
        .then()
            //TODO: cambiar a 201 cuando se implemente correctamente el código de estado para la creación de usuarios
            .statusCode(200)
            .body("data.username", equalTo("testuser"))
            .body("data.email", equalTo("testuser@example.com")); 
    }
}
