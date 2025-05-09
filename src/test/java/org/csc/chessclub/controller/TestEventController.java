package org.csc.chessclub.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.dto.CreateEventDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @Test
    @Order(1)
    void connectionTest() {
        assertNotNull(postgresContainer, "Container should not be null");
        Assertions.assertTrue(postgresContainer.isRunning(), "Container should be running");
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
        given()
                .body(createEventDto)
                .when()
                .post("/events")
                .then()
                .statusCode(201)
                .body("title", equalTo(TITLE))
                .body("description", equalTo(DESCRIPTION))
                .body("author", equalTo(AUTHOR))
                .body("announcementPDF", equalTo(ANNOUNCEMENT_PDF));
    }
}
