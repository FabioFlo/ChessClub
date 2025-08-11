package org.csc.chessclub.controller.user;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.common.mapper.TypeRef;
import java.util.UUID;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.controller.BaseTestConfiguration;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.user.UpdatePasswordDto;
import org.csc.chessclub.exception.ErrorMessage;
import org.csc.chessclub.model.user.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UserExceptionControllerTests extends BaseTestConfiguration {
  String apiPath = "/users";

  private String userToken;
  private UUID userUuid;
  UserEntity unavailableUser;

  @BeforeAll
  void setUp() {
    userToken = getUserToken();
    userUuid = getBaseUserUuid();
    unavailableUser = dataFactory.createUnavailableUser();
  }

  @Test
  @Order(1)
  @DisplayName("Throw when USER try to call get by id")
  void testGetUser_whenUserTryGetUserByID_shouldThrowAccessDeniedException() {
    ResponseDto<ErrorMessage> response =
        authHelper
            .withBearerToken(userToken)
            .pathParam("uuid", userUuid)
            .when()
            .get(apiPath + "/{uuid}")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
            .extract()
            .response()
            .as(new TypeRef<>() {});

    assertThat(response).isNotNull();
    assertThat(response.success()).isFalse();
    assertThat(response.message()).isEqualTo("Access denied");
    assertThat(response.data().statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }

  @Test
  @Order(2)
  @DisplayName("Throw when USER try to call delete another user")
  void testDeleteUser_whenUserTryToDeleteAUser_shouldThrowAccessDeniedException() {
    ResponseDto<ErrorMessage> response =
        authHelper
            .withBearerToken(userToken)
            .pathParam("uuid", unavailableUser.getUuid())
            .when()
            .delete(apiPath + "/{uuid}")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
            .extract()
            .response()
            .as(new TypeRef<>() {});

    assertThat(response).isNotNull();
    assertThat(response.success()).isFalse();
    assertThat(response.message()).isEqualTo("Access denied");
    assertThat(response.data().statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }

  @Test
  @Order(3)
  @DisplayName("Login fail when user available is false")
  void testLogin_whenUserHaveAvailableFalse_shouldThrowException() {
    AuthenticationRequest request =
        new AuthenticationRequest(unavailableUser.getEmail(), "Test123_");
    ResponseDto<ErrorMessage> response =
        given()
            .body(request)
            .when()
            .post(apiPath + "/login")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .extract()
            .response()
            .as(new TypeRef<>() {});

    assertThat(response).isNotNull();
    assertThat(response.success()).isFalse();
    assertThat(response.data().message()).isEqualTo("User is disabled");
    assertThat(response.message()).isEqualTo("Authentication Required");
  }

  @Test
  @Order(4)
  @DisplayName("Throw when USER try to update user password")
  void
      testUpdatePasswordUser_whenUserCallUpdatePasswordDtoProvided_shouldThrowAccessDeniedException() {
    UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto(userUuid, "New_Password1");

    ResponseDto<ErrorMessage> response =
        authHelper
            .withBearerToken(userToken)
            .body(updatePasswordDto)
            .when()
            .patch(apiPath + "/password")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
            .extract()
            .response()
            .as(new TypeRef<>() {});

    assertThat(response).isNotNull();
    assertThat(response.success()).isFalse();
    assertThat(response.message()).isEqualTo("Access denied");
    assertThat(response.data().statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
  }
}
