package org.csc.chessclub.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.exception.validation.ValidErrorMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@ActiveProfiles("test")
public class TestEventValidationController {

    private static final String DESCRIPTION = "Test Description";
    private static final String AUTHOR = "Test Author";
    private static final String ANNOUNCEMENT_PDF = "Test Announcement PDF";
    private final UUID invalidUuid = new UUID(0, 0);
    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private int port;

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
    @DisplayName("Create Event - Should throw validation exception when invalid create event dto provided")
    void testCreateEvent_whenInvalidCreateEventDtoProvided_shouldThrowValidationException() {
        CreateEventDto invalidCreateEventDto = new CreateEventDto(
                null, DESCRIPTION, AUTHOR, ANNOUNCEMENT_PDF);

        ResponseDto<ValidErrorMessage> response = given()
                .body(invalidCreateEventDto)
                .when()
                .post("/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .extracting(ValidErrorMessage::errors)
                .hasFieldOrProperty("title");
    }

    @Test
    @DisplayName("Update Event - Should throw validation exception when invalid event details provided")
    void testUpdateEvent_whenInvalidEventDetailsDtoProvided_shouldThrowValidationException() {
        UpdateEventDto invalidUpdateEventDto = new UpdateEventDto(invalidUuid, null, null, null, "");

        ResponseDto<ValidErrorMessage> response = given()
                .body(invalidUpdateEventDto)
                .when()
                .patch("/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .extracting(ValidErrorMessage::errors)
                .hasFieldOrProperty("title")
                .hasFieldOrProperty("uuid")
                .hasFieldOrProperty("description")
                .hasFieldOrProperty("author");
    }

    @Test
    @DisplayName("Get by Id - Should throw validation exception when invalid uuid provided")
    void testGetEventById_whenInvalidUuidProvided_validErrorMessageShouldReturn() {
        ResponseDto<ValidErrorMessage> response = given()
                .pathParam("uuid", invalidUuid)
                .when()
                .get("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .extracting(ValidErrorMessage::errors)
                .hasFieldOrProperty("uuid");
    }

    @Test
    @DisplayName("Delete - Should throw validation exception when invalid uuid provided")
    void testDeleteEvent_whenInvalidUuidProvided_validErrorMessageShouldReturn() {
        ResponseDto<ValidErrorMessage> response = given()
                .pathParam("uuid", invalidUuid)
                .when()
                .delete("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .extracting(ValidErrorMessage::errors)
                .hasFieldOrProperty("uuid");
    }
}
