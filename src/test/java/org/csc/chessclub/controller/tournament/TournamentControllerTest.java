package org.csc.chessclub.controller.tournament;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
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
    @Autowired
    private TournamentRepository tournamentRepository;

  @BeforeAll
  public void beforeAll() {
    tournamentRepository.deleteAll();
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
    UpdateTournamentDto updateTournamentDto = new UpdateTournamentDto(uuid, newTitle, startDate,
        endDate, "Description", null);
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

  @Test
  @Order(3)
  @DisplayName("Get by id")
  void getTournamentById_whenValidIdProvided_returnGetTournamentDtoIfExists() {
    ResponseDto<TournamentDto> response = given()
        .pathParam("uuid", uuid)
        .when()
        .get(apiPath + "/{uuid}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::success).isEqualTo(true);

    assertThat(response.data().uuid())
        .isEqualTo(uuid);
  }

  @Test
  @Order(4)
  @DisplayName("Get all paged")
  void testGetAll_whenTournamentsExists_returnsTournamentsDto() {
    ResponseDto<PageResponseDto<TournamentDto>> response = given()
        .when()
        .get(apiPath)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::success).isEqualTo(true);

    assertThat(response.data().pageSize())
        .isEqualTo(10);

    assertThat(response)
        .extracting(ResponseDto::data)
        .extracting(PageResponseDto::content)
        .extracting(List::size).isEqualTo(1);

  }

  @Test
  @Order(5)
  @DisplayName("Delete tournament")
  void testDeleteTournament_whenUserAuthenticatedAndGameFound_returnResponseDtoWithSuccessTrue() {
    ResponseDto<UUID> response = given()
        .pathParam("uuid", uuid)
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
        .extracting(UUID::toString).isEqualTo(uuid.toString());
  }
}
