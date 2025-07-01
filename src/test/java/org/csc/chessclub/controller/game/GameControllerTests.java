package org.csc.chessclub.controller.game;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.enums.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTests extends BaseIntegrationTest {

    private final String apiPath = "/games";
    private String userToken;
    @Value("${user.username}")
    private String userUsername;
    @Value("${user.password}")
    private String userPassword;
    private CreateGameDto createGameDto;
    private UUID gameId;
    private final String whitePlayer = "White player";

    @BeforeAll
    public void setUp() {
        createGameDto = new CreateGameDto("", "", "game pgn", Result.BlackWon);

        AuthenticationRequest userLogin = new AuthenticationRequest(userUsername, userPassword);
        userToken = loginAndGetResponse(userLogin).data().token();
    }

    @Test
    @Order(1)
    @DisplayName("Create Game correctly")
    void testCreateGame_whenUserAuthenticatedAndValidCreateGameProvided_returnsCreatedGame() {
        ResponseDto<GameDto> response = given()
                .header("Authorization", "Bearer " + userToken)
                .body(createGameDto)
                .when()
                .post(apiPath)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        gameId = response.data().uuid();

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo("Game created");
    }

    @Test
    @Order(2)
    @DisplayName("Update Game correctly")
    void testUpdateGame_whenUserAuthenticatedAndValidUpdateGameProvided_returnsUpdatedGame() {
        UpdateGameDto updateGameDto = new UpdateGameDto(gameId, whitePlayer, "Black player", "", Result.WhiteWon);
        ResponseDto<GameDto> response = given()
                .header("Authorization", "Bearer " + userToken)
                .body(updateGameDto)
                .when()
                .patch(apiPath)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(GameDto::whitePlayerName)
                .isEqualTo(whitePlayer);

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo("Game updated");
    }

    @Test
    @Order(3)
    @DisplayName("Get game by id")
    void testGetGameById_whenExistingIdProvided_returnsGame() {
        ResponseDto<GameDto> response = given()
                .pathParam("uuid", gameId)
                .when()
                .get(apiPath + "/{uuid}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(GameDto::uuid).isEqualTo(gameId);
    }

    @Test
    @Order(4)
    @DisplayName("Get all paged")
    void testGetAll_whenGamesExists_returnsAllGames() {
        ResponseDto<PageResponseDto<GameDto>> response = given()
                .when()
                .get(apiPath)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(PageResponseDto::pageSize).isEqualTo(10);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(PageResponseDto::content)
                .extracting(List::size).isEqualTo(1);
    }

    @Test
    @Order(5)
    @DisplayName("Games by player name")
    void testGamesByPlayerName_whenPlayerNameProvided_returnsGamesByPlayerName() {
        String playerName = whitePlayer;
        Pageable pageable = PageRequest.of(0, 5);

        ResponseDto<PageResponseDto<GameDto>> response = withPageable(pageable)
                .when()
                .pathParams("player-name", playerName)
                .get(apiPath + "/player/{player-name}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(PageResponseDto::content)
                .extracting(List::size).isEqualTo(1);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(PageResponseDto::pageSize).isEqualTo(5);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(PageResponseDto::content)
                .extracting(List::getFirst)
                .extracting(GameDto::whitePlayerName).isEqualTo(playerName);
    }

    @Test
    @Order(6)
    @DisplayName("Delete game")
    void testDeleteGame_whenUserAuthenticatedAndGameFound_returnResponseDtoWithSuccessTrue() {
        ResponseDto<UUID> response = given()
                .pathParam("uuid", gameId)
                .header("Authorization", "Bearer " + userToken)
                .when()
                .delete(apiPath + "/{uuid}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response)
                .extracting(ResponseDto::data)
                .extracting(UUID::toString).isEqualTo(gameId.toString());
    }
}
