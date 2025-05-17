package org.csc.chessclub.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.ErrorMessage;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
public class TestEventExceptionController {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private int port;

    private final UUID notFoundUuid = UUID.randomUUID();
    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @Test
    @Order(1)
    void connectionTest() {
        assertNotNull(postgresContainer, "Container should not be null");
        assertTrue(postgresContainer.isRunning(), "Container should be running");
    }

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @Test
    @Order(2)
    @DisplayName("Get Event By Id - Throw Not found exception")
    void testGetEvent_whenEventNotFound_shouldThrowCustomNotFoundExceptionFromGlobalExceptionHandler() {
        ResponseDto<ErrorMessage> response = given()
                .pathParam("uuid", notFoundUuid)
                .when()
                .get("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response.success()).isFalse();
        assertThat(response.message()).isEqualTo("Not found");
        assertThat(response.data().message()).isEqualTo(NotFoundMessage.EVENT_WITH_UUID.getMessage().formatted(notFoundUuid));
        assertThat(response.data().statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Get Event By Id - Throw Bad Request exception")
    void testGetEvent_whenInvalidUuid_shouldThrowCustomBadRequestExceptionFromGlobalExceptionHandler() {
        ResponseDto<ErrorMessage> response = given()
                .pathParam("uuid", "invalidUuid")
                .when()
                .get("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(new TypeRef<>() {});

        assertThat(response.success()).isFalse();
        assertThat(response.message()).isEqualTo("Type mismatch");
        assertThat(response.data().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
