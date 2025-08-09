package org.csc.chessclub.controller.user;

import io.restassured.common.mapper.TypeRef;
import org.assertj.core.api.Assertions;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.auth.AuthenticationResponse;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.user.*;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTests extends BaseIntegrationTest {

  String apiPath = "/users";

  private final RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL,
      PASSWORD);

  private static final String USERNAME = "Test Username";
  private static final String PASSWORD = "Password1_";
  private static final String EMAIL = "email@email.com";
  private String adminToken;
  private String userToken;
  private UUID userUuid;

  private static final String CREATED = "User successfully created";
  private static final String LOGGED_IN = "User successfully logged in";
  private static final String UPDATED = "User successfully updated";
  private static final String DELETED = "User deleted";
  private static final String UPDATED_ROLE = "Role successfully updated";
  private static final String UPDATED_PASSWORD = "Password successfully updated";

  @Autowired
  private JwtService service;

  @Test
  @Order(1)
  @DisplayName("Admin Login")
  void testAdminLogin_whenValidUserProvided_returnsUser() {
    AuthenticationRequest request = new AuthenticationRequest("admin", "Admin123_");

    ResponseDto<AuthenticationResponse> response =
        loginAndGetResponse(request);

    adminToken = response.data().token();

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::message)
        .isEqualTo(LOGGED_IN);

    assertNotNull(adminToken);
    assertThat(response)
        .extracting(ResponseDto::data)
        .extracting(AuthenticationResponse::token)
        .isEqualTo(adminToken);
    String usernameOrEmail = service.extractUsernameOrEmail(adminToken);
    assertEquals("admin", usernameOrEmail);
  }

  @Test
  @Order(2)
  @DisplayName("Register User")
  void testRegisterUser_whenAuthenticatedAdminAndValidUserProvided_returnsRegisteredUser() {
    ResponseDto<UserDto> response = given()
        .header("Authorization", "Bearer " + adminToken)
        .body(registerUserRequest)
        .when()
        .post(apiPath)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::data)
        .extracting(UserDto::username)
        .isEqualTo(USERNAME);

    assertThat(response)
        .extracting(ResponseDto::message)
        .isEqualTo(CREATED);
  }

  @Test
  @Order(3)
  @DisplayName("User login")
  void testUserLogin_whenValidUserProvided_returnsUser() {
    AuthenticationRequest request = new AuthenticationRequest(EMAIL, PASSWORD);

    ResponseDto<AuthenticationResponse> response =
        loginAndGetResponse(request);

    userToken = response.data().token();
    assertNotNull(userToken);

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::message)
        .isEqualTo(LOGGED_IN);

    String usernameOrEmail = service.extractUsernameOrEmail(userToken);
    assertEquals(USERNAME, usernameOrEmail);
    userUuid = UUID.fromString(service.extractId(userToken));
    assertNotNull(userUuid);
  }

  @Test
  @Order(4)
  @DisplayName("User can update his entity")
  void testUpdateUser_whenAuthenticatedAndValidUpdateUserProvided_returnsUpdatedUser() {
    UpdateUserRequest updateUser = new UpdateUserRequest(userUuid, "NewUsername", EMAIL);

    ResponseDto<UserDto> response = given()
        .header("Authorization", "Bearer " + userToken)
        .body(updateUser)
        .when()
        .patch(apiPath)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::message)
        .isEqualTo(UPDATED);

  }

  @Test
  @Order(5)
  @DisplayName("Admin can get user by id")
  void testGetUser_whenAuthenticatedAdminAndValidUuidProvided_returnUserDto() {
    ResponseDto<UserDto> response = given()
        .header("Authorization", "Bearer " + adminToken)
        .pathParam("uuid", userUuid)
        .when()
        .get(apiPath + "/{uuid}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::message)
        .isEqualTo("User found");
  }

  @Test
  @Order(6)
  @DisplayName("Admin can update user")
  void testUpdateUser_whenAuthenticatedAdminAndValidUpdateUserProvided_returnsUpdatedUser() {
    UpdateUserRequest updateUser = new UpdateUserRequest(userUuid, USERNAME, EMAIL);

    ResponseDto<UserDto> response = given()
        .header("Authorization", "Bearer " + adminToken)
        .body(updateUser)
        .when()
        .patch(apiPath)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::message)
        .isEqualTo(UPDATED);
  }

  @Test
  @Order(7)
  @DisplayName("Admin can update user role")
  void testUpdateUser_whenAuthenticatedAdminAndValidUpdateRoleDtoProvided_returnsUpdatedRole() {
    Role oldRole = Role.USER;
    Role newRole = Role.ADMIN;
    UpdateRoleDto updateRole = new UpdateRoleDto(userUuid, oldRole, newRole);

    ResponseDto<Role> response = given()
        .header("Authorization", "Bearer " + adminToken)
        .body(updateRole)
        .when()
        .patch(apiPath + "/role")
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::message)
        .isEqualTo(UPDATED_ROLE);
  }

  @Test
  @Order(8)
  @DisplayName("Admin can delete user")
  void testDeleteUser_whenAuthenticatedAdminAndValidUuidProvided_returnsDeletedUser() {
    ResponseDto<UUID> response = given()
        .header("Authorization", "Bearer " + adminToken)
        .pathParam("uuid", userUuid)
        .when()
        .delete(apiPath + "/{uuid}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::message)
        .isEqualTo(DELETED);

  }

  @Test
  @Order(9)
  @DisplayName("Get all paged users")
  void testGetAll_whenUsersExists_returnsAllUsersPaged() {
    ResponseDto<PageResponseDto<UserDto>> response = given()
        .header("Authorization", "Bearer " + adminToken)
        .when()
        .get(apiPath)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract().response().as(new TypeRef<>() {
        });

    Assertions.assertThat(response)
        .isNotNull()
        .extracting(ResponseDto::success).isEqualTo(true);

    Assertions.assertThat(response.data().pageSize())
        .isEqualTo(10);

    Assertions.assertThat(response)
        .extracting(ResponseDto::data)
        .extracting(PageResponseDto::content)
        .extracting(List::size).isEqualTo(3);
  }

    @Test
    @Order(10)
    @DisplayName("Admin can update user password")
    void testUpdateUserPassword_whenAuthenticatedAdminAndValidUpdatePasswordDtoProvided_returnsUpdatedPassword() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto(userUuid, "New_Password1");

        ResponseDto<Role> response = given()
                .header("Authorization", "Bearer " + adminToken)
                .body(updatePasswordDto)
                .when()
                .patch(apiPath + "/password")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::message)
                .isEqualTo(UPDATED_PASSWORD);
    }
}
