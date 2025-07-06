package org.csc.chessclub.controller.tournament;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
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
    private UUID uuid;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeAll
    public void beforeAll() {
        startDate = LocalDate.parse("2020-01-01");
        endDate = LocalDate.parse("2020-01-03");
        createTournamentDto = new CreateTournamentDto(
                "New tournament",
                startDate,
                endDate,
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

        uuid = response.data().uuid();

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo(expectedMessage);
    }

    @Test
    @Order(2)
    @DisplayName("Update tournament")
    void testUpdateTournament_whenUserAuthenticatedAndValidUpdateTournamentProvided_returnUpdateTournamentDto() {
        String expectedMessage = "Tournament successfully updated";
        String newTitle = "New test title";
        UpdateTournamentDto updateTournamentDto = new UpdateTournamentDto(uuid, newTitle, startDate, endDate, "Description", null);
        ResponseDto<TournamentDto> response = given()
                .header("Authorization", "Bearer " + userToken)
                .body(updateTournamentDto)
                .when()
                .patch(apiPath)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::success).isEqualTo(true);

        assertThat(response.data().title())
                .isEqualTo(newTitle);

        assertThat(response)
                .extracting(ResponseDto::message)
                .isEqualTo(expectedMessage);
    }
    //TODO: get by id
    //TODO: get all paged
    //TODO: delete
}
