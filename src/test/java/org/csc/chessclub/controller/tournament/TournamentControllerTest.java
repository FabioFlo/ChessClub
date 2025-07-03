package org.csc.chessclub.controller.tournament;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class TournamentControllerTest extends BaseIntegrationTest {

    private final String apiPath = "/tournaments";
    private String userToken;
    @Value("${user.username}")
    private String userUsername;
    @Value("${user.password}")
    private String userPassword;
    private CreateTournamentDto createTournamentDto;
    private UUID id;

    @BeforeAll
    public void beforeAll() {
        createTournamentDto = new CreateTournamentDto(
                "New tournament",
                LocalDate.parse("2020-01-01"),
                LocalDate.parse("2020-01-03"),
                "Description",
                null);

        AuthenticationRequest userLogin = new AuthenticationRequest(userUsername, userPassword);
        userToken = loginAndGetResponse(userLogin).data().token();
    }

    @Test
    @Order(1)
    @DisplayName("Create tournament")
    void testCreateTournament_whenUserAuthenticatedAndValidCreateTournamentProvided_returnCreateTournamentDto() {
        String expectedMessage = "Tournament successfully created";

        ResponseDto<TournamentDto> response = given()
                .header("Authorization", "Bearer " + userToken)
                .body(createTournamentDto)
                .when()
                .post(apiPath)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success)
                .isEqualTo(true);

        id = response.data().uuid();

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo(expectedMessage);
    }
}
