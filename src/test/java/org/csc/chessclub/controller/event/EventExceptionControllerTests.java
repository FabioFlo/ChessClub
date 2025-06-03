package org.csc.chessclub.controller.event;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventExceptionControllerTests extends BaseIntegrationTest {

    private final UUID notFoundUuid = UUID.randomUUID();

    @Test
    @Order(1)
    void connectionTest() {
        assertTrue(isContainerNotNullAndRunning(),
                "Container should be not null and running");
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
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response.success()).isFalse();
        assertThat(response.message()).isEqualTo("Type mismatch");
        assertThat(response.data().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
