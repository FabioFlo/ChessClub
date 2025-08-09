package org.csc.chessclub.controller.user;

import io.restassured.common.mapper.TypeRef;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.auth.AuthenticationResponse;
import org.csc.chessclub.controller.BaseIntegrationTest;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.exception.ErrorMessage;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class UserExceptionControllerTests extends BaseIntegrationTest {
    String apiPath = "/users";
    private String userToken;
    private UUID userUuid;
    private UserEntity unavailableUser;

    AuthenticationRequest request = new AuthenticationRequest("user", "User123_");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setUp() {
        ResponseDto<AuthenticationResponse> userAuth = loginAndGetResponse(request);
        userToken = userAuth.data().token();
        userUuid = userRepository.findUserEntityByUsernameOrEmail("user", "user").get().getUuid();

        unavailableUser = userRepository.save(UserEntity
                .builder()
                .username("unavailable")
                .email("unavailable@email.it")
                .password(passwordEncoder.encode("Password_1"))
                .role(Role.USER)
                .available(false)
                .build());

    }

    @Test
    @Order(1)
    @DisplayName("Throw when USER try to call get by id")
    void testGetUser_whenUserTryGetUserByID_shouldThrowAccessDeniedException() {
        ResponseDto<ErrorMessage> response = given()
                .header("Authorization", "Bearer " + userToken)
                .pathParam("uuid", userUuid)
                .when()
                .get(apiPath + "/{uuid}")
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
    @Order(2)
    @DisplayName("Throw when USER try to call delete another user")
    void testDeleteUser_whenUserTryToDeleteAUser_shouldThrowAccessDeniedException() {
        ResponseDto<ErrorMessage> response = given()
                .header("Authorization", "Bearer " + userToken)
                .pathParam("uuid", unavailableUser.getUuid())
                .when()
                .delete(apiPath + "/{uuid}")
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
    @Order(3)
    @DisplayName("Login fail when user available is false")
    void testLogin_whenUserHaveAvailableFalse_shouldThrowException() {
        AuthenticationRequest request = new AuthenticationRequest(unavailableUser.getEmail(), unavailableUser.getPassword());

        ResponseDto<ErrorMessage> response =
                given()
                        .body(request)
                        .when()
                        .post(apiPath + "/login")
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
