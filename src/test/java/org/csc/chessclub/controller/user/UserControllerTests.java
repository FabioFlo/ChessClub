package org.csc.chessclub.controller.user;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.auth.AuthenticationResponse;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.exception.ErrorMessage;
import org.csc.chessclub.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTests extends BaseIntegrationTest {

    private final RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "Password1_";
    private static final String EMAIL = "email@email.com";
    private String adminToken = "";
    private String userToken = "";
    private UUID userUuid = UUID.randomUUID();

    private static final String CREATED = "User successfully created";
    private static final String LOGGED_IN = "User successfully logged in";
    private static final String UPDATED = "User successfully updated";
    private static final String DELETED = "User deleted";

    @Autowired
    private JwtService service;


    @Test
    @Order(2)
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
    @Order(3)
    @DisplayName("Register User")
    void testRegisterUser_whenAuthenticatedAdminAndValidUserProvided_returnsRegisteredUser() {
        ResponseDto<UserDto> response = given()
                .header("Authorization", "Bearer " + adminToken)
                .body(registerUserRequest)
                .when()
                .post("/users")
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
    @Order(4)
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
    @Order(5)
    @DisplayName("Throw when USER try to call get by id")
    void testGetUser_whenUserTryGetUserByID_shouldThrowAccessDeniedException() {
        ResponseDto<ErrorMessage> response = given()
                .header("Authorization", "Bearer " + userToken)
                .pathParam("uuid", userUuid)
                .when()
                .get("/users/{uuid}")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull();
        assertThat(response.success()).isFalse();
        assertThat(response.message()).isEqualTo("Access denied");
        assertThat(response.data().statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(6)
    @DisplayName("Throw when USER try to call delete user")
    void testDeleteUser_whenUserTryToDeleteAUser_shouldThrowAccessDeniedException() {
        ResponseDto<ErrorMessage> response = given()
                .header("Authorization", "Bearer " + userToken)
                .pathParam("uuid", userUuid)
                .when()
                .delete("/users/{uuid}")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract().response().as(new TypeRef<>() {
                });

        assertThat(response)
                .isNotNull();
        assertThat(response.success()).isFalse();
        assertThat(response.message()).isEqualTo("Access denied");
        assertThat(response.data().statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(7)
    @DisplayName("User can update his data")
    void testUpdateUser_whenAuthenticatedAndValidUpdateUserProvided_returnsUpdatedUser() {
        UpdateUserRequest updateUser = new UpdateUserRequest(userUuid, "NewUsername", EMAIL);

        ResponseDto<UserDto> response = given()
                .header("Authorization", "Bearer " + userToken)
                .body(updateUser)
                .when()
                .patch("/users")
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
    @Order(8)
    @DisplayName("Admin can get user by id")
    void testGetUser_whenAuthenticatedAdminAndValidUuidProvided_returnUserDto() {
        ResponseDto<UserDto> response = given()
                .header("Authorization", "Bearer " + adminToken)
                .pathParam("uuid", userUuid)
                .when()
                .get("/users/{uuid}")
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
    @Order(9)
    @DisplayName("Admin can update user")
    void testUpdateUser_whenAuthenticatedAdminAndValidUpdateUserProvided_returnsUpdatedUser() {
        UpdateUserRequest updateUser = new UpdateUserRequest(userUuid, USERNAME, EMAIL);

        ResponseDto<UserDto> response = given()
                .header("Authorization", "Bearer " + adminToken)
                .body(updateUser)
                .when()
                .patch("/users")
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
    @Order(10)
    @DisplayName("Admin can delete user")
    void testDeleteUser_whenAuthenticatedAdminAndValidUuidProvided_returnsDeletedUser() {
        ResponseDto<UUID> response = given()
                .header("Authorization", "Bearer " + adminToken)
                .pathParam("uuid", userUuid)
                .when()
                .delete("/users/{uuid}")
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
    @Order(11)
    @DisplayName("Login fail when user available is false")
    void testLogin_whenUserHaveAvailableFalse_shouldThrowException() {
        AuthenticationRequest request = new AuthenticationRequest(EMAIL, PASSWORD);

        ResponseDto<ErrorMessage> response =
                given()
                        .body(request)
                        .when()
                        .post("/users/login")
                        .then()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract().response().as(new TypeRef<>() {
                        });

        assertThat(response)
                .isNotNull();
        assertThat(response.success()).isFalse();
        assertThat(response.data().message()).isEqualTo("User is disabled");
        assertThat(response.message()).isEqualTo("Authentication Required");
    }
}
