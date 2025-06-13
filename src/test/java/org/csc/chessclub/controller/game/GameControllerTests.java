package org.csc.chessclub.controller.game;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.enums.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTests extends BaseIntegrationTest {

    private String userToken;
    @Value("${user.username}")
    private String userUsername;
    @Value("${user.password}")
    private String userPassword;
    private CreateGameDto createGameDto;

    @BeforeAll
    public void setUp() {
        createGameDto = new CreateGameDto("", "", "game pgn", Result.BlackWon);

        AuthenticationRequest userLogin = new AuthenticationRequest(userUsername, userPassword);
        userToken = loginAndGetResponse(userLogin).data().token();
    }

    @Test
    @Order(1)
    @DisplayName("Create Game")
    void testCreateGame_whenUserAuthenticatedAndValidCreateGameProvided_returnsCreatedGame() {
        ResponseDto<GameDto> response = given()
                .header("Authorization", "Bearer " + userToken)
                .body(createGameDto)
                .when()
                .post("/games")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo("Game created");
    }
}
