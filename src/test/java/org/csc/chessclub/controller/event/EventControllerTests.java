package org.csc.chessclub.controller.event;

import io.restassured.common.mapper.TypeRef;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.csc.chessclub.controller.BaseTestConfiguration;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventControllerTests extends BaseTestConfiguration {

    private CreateEventDto createEventDto;
    private static final String TITLE = "Test Title";
    private static final String DESCRIPTION = "Test Description";
    private static final String AUTHOR = "Test Author";
    private UUID uuid;

    private String userToken;
    @Value("${storage.pdf-folder}")
    String storageFolder;

    private static final String CREATED = "Event successfully created";
    private static final String DELETED = "Event deleted";

    @BeforeAll
    void setup() {
        createEventDto = new CreateEventDto(
                TITLE, DESCRIPTION, AUTHOR);

        userToken = getUserToken();
    }

    @Test
    @Order(2)
    @DisplayName("Create Event")
    void testCreateEvent_whenUserAuthenticatedAndValidDetailsProvided_returnsCreatedEvent() {
        ResponseDto<EventDto> response = withBearerToken(userToken)
                .multiPart("event", "event.json", createEventDto, "application/json")
                .multiPart("file", new File(storageFolder + "/announcement.pdf"))
                .contentType("multipart/form-data")
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
                .isEqualTo(CREATED);

    }

    @Test
    @Order(3)
    @DisplayName("Get all Events available")
    void testGetAllEvents_whenEventsFound_returnsAllEvents() {
        ResponseDto<PageResponseDto<EventDto>> response = given()
                .when()
                .get("/events")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data)
                .extracting(PageResponseDto::content)
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .isNotEmpty();

        uuid = response.data().content().getFirst().uuid();
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
    void testUpdateEvent_whenUserAuthenticatedAndValidEventDetailsProvided_returnsUpdatedEvent() {
        String newTitle = "New test title";
        UpdateEventDto updateEventDto = new UpdateEventDto(uuid, newTitle, DESCRIPTION, AUTHOR);

        ResponseDto<EventDto> response = withBearerToken(userToken)
                .multiPart("event", "event.json", updateEventDto, "application/json")
                .multiPart("file", new File(storageFolder + "/announcement.pdf"))
                .contentType("multipart/form-data")
                .when()
                .patch("/events")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::data).extracting(EventDto::title)
                .isEqualTo(newTitle);
    }

    @Test
    @Order(6)
    @DisplayName("Delete event")
    void testDeleteEvent_whenUserAuthenticatedAndEventFound_returnsResponseDtoWithSuccessAndMessage() {
        ResponseDto<UUID> responseDto = withBearerToken(userToken)
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
                .isEqualTo(DELETED);

        assertThat(responseDto)
                .extracting(ResponseDto::data)
                .isEqualTo(uuid);
    }

}
