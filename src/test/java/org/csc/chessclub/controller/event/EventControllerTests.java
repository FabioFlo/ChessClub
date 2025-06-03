package org.csc.chessclub.controller.event;

import io.restassured.common.mapper.TypeRef;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class EventControllerTests extends BaseIntegrationTest {

    private CreateEventDto createEventDto;
    private static final String TITLE = "Test Title";
    private static final String DESCRIPTION = "Test Description";
    private static final String AUTHOR = "Test Author";
    private static final String ANNOUNCEMENT_PDF = "Test Announcement PDF";
    private UUID uuid;

    @BeforeAll
    void setup() {
        createEventDto = new CreateEventDto(
                TITLE, DESCRIPTION, AUTHOR, ANNOUNCEMENT_PDF);
    }

    @Test
    @Order(1)
    void connectionTest() {
        assertTrue(isContainerNotNullAndRunning(),
                "Container should be not null and running");
    }

    @Test
    @Order(2)
    @DisplayName("Create Event")
    void testCreateEvent_whenValidDetailsProvided_returnsCreatedEvent() {
        ResponseDto<EventDto> response = given()
                .body(createEventDto)
                .when()
                .post("/events")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo("Event created");

    }

    @Test
    @Order(3)
    @DisplayName("Get all Events")
    void testGetAllEvents_whenEventsFound_returnsAllEvents() {
        ResponseDto<List<EventDto>> response = given()
                .when()
                .get("/events")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .isNotEmpty();

        uuid = response.data().getFirst().uuid();
        assertNotNull(uuid, "UUID should not be null");
    }

    @Test
    @Order(4)
    @DisplayName("Get Event By Id")
    void testGetEvent_whenEventFoundById_returnsEvent() {
        ResponseDto<EventDto> response = given()
                .pathParam("uuid", uuid)
                .when()
                .get("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        EventDto event = response.data();
        assertThat(event)
                .isNotNull();
        assertEquals(event.uuid(), uuid);
    }

    @Test
    @Order(5)
    @DisplayName("Update event")
    void testUpdateEvent_whenValidEventDetailsProvided_returnsUpdatedEvent() {
        UpdateEventDto updateEventDto = new UpdateEventDto(uuid, "New test title", DESCRIPTION, AUTHOR, ANNOUNCEMENT_PDF);

        ResponseDto<UpdateEventDto> response = given()
                .body(updateEventDto)
                .when()
                .patch("/events")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .isEqualTo(updateEventDto);
    }

    @Test
    @Order(6)
    @DisplayName("Delete event")
    void testDeleteEvent_whenEventFound_returnsResponseDtoWithSuccessAndMessage() {
        ResponseDto<UUID> responseDto = given()
                .pathParam("uuid", uuid)
                .when()
                .delete("/events/{uuid}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(responseDto)
                .isNotNull()
                .extracting(ResponseDto::message)
                .isEqualTo("Event deleted");

        assertThat(responseDto)
                .extracting(ResponseDto::data)
                .isEqualTo(uuid);
    }

}
