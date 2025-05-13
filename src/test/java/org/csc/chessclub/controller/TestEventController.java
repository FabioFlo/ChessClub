package org.csc.chessclub.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.dto.EventDetailsDto;
import org.csc.chessclub.dto.GetEventDto;
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
public class TestEventController {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private int port;

    private CreateEventDto createEventDto;
    private static final String TITLE = "Test Title";
    private static final String DESCRIPTION = "Test Description";
    private static final String AUTHOR = "Test Author";
    private static final String ANNOUNCEMENT_PDF = "Test Announcement PDF";
    private UUID uuid;

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

        createEventDto = new CreateEventDto(
                TITLE, DESCRIPTION, AUTHOR, ANNOUNCEMENT_PDF);

    }

    @Test
    @Order(2)
    @DisplayName("Create Event")
    void testCreateEvent_whenValidDetailsProvided_returnsCreatedEvent() {
        boolean eventCreated = given()
                .body(createEventDto)
                .when()
                .post("/events")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response().asString().contains("Event created");

        assertTrue(eventCreated, "Event should be created");

    }

    @Test
    @Order(3)
    @DisplayName("Get all Events")
    void testGetAllEvents_whenEventsFound_returnsAllEvents() {
        GetEventDto[] events = given()
                .when()
                .get("/events")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(GetEventDto[].class);

        assertThat(events)
                .isNotNull()
                .isNotEmpty();

        uuid = events[0].uuid();
        assertNotNull(uuid, "UUID should not be null");
    }

    @Test
    @Order(4)
    @DisplayName("Get Event By Id")
    void testGetEvent_whenEventFoundById_returnsEvent() {
        GetEventDto event = given()
                .pathParam("uuid", uuid)
                .when()
                .get("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(GetEventDto.class);

        assertThat(event)
                .isNotNull();
        assertEquals(event.uuid(), uuid);
    }

    @Test
    @Order(5)
    @DisplayName("Update event")
    void testUpdateEvent_whenValidEventDetailsProvided_returnsUpdatedEvent() {
        EventDetailsDto eventDetailsDto = new EventDetailsDto(uuid, "New test title", DESCRIPTION, AUTHOR, ANNOUNCEMENT_PDF);
        given()
                .body(eventDetailsDto)
                .when()
                .patch("/events")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("uuid", equalTo(uuid.toString()))
                .body("title", equalTo("New test title"));
    }
}
