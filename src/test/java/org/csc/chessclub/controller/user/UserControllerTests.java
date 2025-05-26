package org.csc.chessclub.controller.user;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
public class UserControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private int port;

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    private RegisterUserRequest registerUserRequest;

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "Password1_";
    private static final String EMAIL = "email@email.com";

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);
    }

    @Test
    @Order(1)
    void connectionTest() {
        assertNotNull(postgresContainer, "Container should not be null");
        assertTrue(postgresContainer.isRunning(), "Container should be running");
    }

    @Test
    @Order(2)
    @DisplayName("Register User")
    void testRegisterUser_whenValidUserProvided_returnsRegisteredUser() {
        ResponseDto<UserDto> response = given()
                .body(registerUserRequest)
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .extracting(UserDto::username)
                .isEqualTo(USERNAME);

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo("User registered");
    }
}
