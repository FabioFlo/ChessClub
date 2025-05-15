package org.csc.chessclub.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.dto.EventDetailsDto;
import org.csc.chessclub.exception.ValidErrorMessage;
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

        ValidErrorMessage validErrorMessage = given()
                .body(invalidCreateEventDto)
                .when()
                .post("/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(ValidErrorMessage.class);

        assertThat(validErrorMessage.errors())
                .containsEntry("title", "Title must not be blank");

    }

    @Test
    @DisplayName("Update Event - Should throw validation exception when invalid event details provided")
    void testUpdateEvent_whenInvalidEventDetailsDtoProvided_shouldThrowValidationException() {
        EventDetailsDto invalidUpdateEventDto = new EventDetailsDto(invalidUuid, null, null, null, "");

        ValidErrorMessage validErrorMessage = given()
                .body(invalidUpdateEventDto)
                .when()
                .patch("/events")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(ValidErrorMessage.class);

        assertThat(validErrorMessage.errors())
                .containsEntry("uuid", "UUID must be valid")
                .containsEntry("title", "Title must not be blank")
                .containsEntry("description", "Description must not be blank")
                .containsEntry("author", "Author must not be blank")
                .doesNotContainKey("announcementPDF");
    }

    @Test
    @DisplayName("Get by Id - Should throw validation exception when invalid uuid provided")
    void testGetEventById_whenInvalidUuidProvided_validErrorMessageShouldReturn() {
        ValidErrorMessage validErrorMessage = given()
                .pathParam("uuid", invalidUuid)
                .when()
                .get("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(ValidErrorMessage.class);

        assertThat(validErrorMessage.errors())
                .containsEntry("uuid", "Invalid UUID format");
    }

    @Test
    @DisplayName("Delete - Should throw validation exception when invalid uuid provided")
    void testDeleteEvent_whenInvalidUuidProvided_validErrorMessageShouldReturn() {
        ValidErrorMessage validErrorMessage = given()
                .pathParam("uuid", invalidUuid)
                .when()
                .delete("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().as(ValidErrorMessage.class);

        assertThat(validErrorMessage.errors())
                .containsEntry("uuid", "Invalid UUID format");
    }
}
