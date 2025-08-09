package org.csc.chessclub.controller.event;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.controller.BaseTestConfiguration;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.exception.validation.messages.ValidErrorMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class EventValidationControllerTests extends BaseTestConfiguration {

    private static final String DESCRIPTION = "Test Description";
    private static final String AUTHOR = "Test Author";
    private final UUID invalidUuid = new UUID(0, 0);
    private String userToken;
    @Value("${storage.pdf-folder}")
    String storageFolder;

    @BeforeAll
    void setup() {
        userToken = getUserToken();
    }

    @Test
    @DisplayName("Create Event - Should throw validation exception when invalid create event dto provided")
    void testCreateEvent_whenInvalidCreateEventDtoProvided_shouldThrowValidationException() {
        CreateEventDto invalidCreateEventDto = new CreateEventDto(
                null, DESCRIPTION, AUTHOR);

        ResponseDto<ValidErrorMessage> response = withBearerToken(userToken)
                .multiPart("event", "event.json", invalidCreateEventDto, "application/json")
                .contentType("multipart/form-data")
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
        UpdateEventDto invalidUpdateEventDto = new UpdateEventDto(invalidUuid, null, null, null);

        ResponseDto<ValidErrorMessage> response = given()
                .multiPart("event", "event.json", invalidUpdateEventDto, "application/json")
                .contentType("multipart/form-data")
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
        ResponseDto<ValidErrorMessage> response = withBearerToken(userToken)
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


    @Test
    @DisplayName("Update event with file - fails if format not allowed")
    void testUpdateEvent_whenFormatNotAllowed_thenValidationFails() {
        UpdateEventDto updEventDto = new UpdateEventDto(UUID.randomUUID(), "title", "description",
                "author");
        String notAllowedFormat = "notAllowedFormat.jpeg";

        ResponseDto<ValidErrorMessage> response = withBearerToken(userToken)
                .multiPart("event", "event.json", updEventDto, "application/json")
                .multiPart("file", new File(storageFolder + "/" + notAllowedFormat))
                .contentType("multipart/form-data")
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
                .hasFieldOrProperty("file");
    }

}
