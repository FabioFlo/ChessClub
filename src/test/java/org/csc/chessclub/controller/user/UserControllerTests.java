package org.csc.chessclub.controller.user;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.auth.AuthenticationResponse;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.csc.chessclub.security.JwtService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
public class UserControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private int port;

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    private RegisterUserRequest registerUserRequest;

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "Password1_";
    private static final String EMAIL = "email@email.com";
    private String adminToken = "";
    private String userToken = "";
    private UUID userUuid = UUID.randomUUID();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService service;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);

        userRepository.save(UserEntity.
                builder()
                .username("admin")
                .email("admin@admin.com")
                .password(passwordEncoder.encode("Admin123_"))
                .role(Role.ADMIN)
                .available(true).build());
    }

    @Test
    @Order(1)
    void connectionTest() {
        assertNotNull(postgresContainer, "Container should not be null");
        assertTrue(postgresContainer.isRunning(), "Container should be running");
    }

    @Test
    @Order(2)
    @DisplayName("Admin Login")
    void testAdminLogin_whenValidUserProvided_returnsUser() {
        AuthenticationRequest request = new AuthenticationRequest("admin", "Admin123_");

        ResponseDto<AuthenticationResponse> response =
                given()
                        .body(request)
                        .when()
                        .post("/users/login")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract().response().as(new TypeRef<>() {
                        });

        adminToken = response.data().token();

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::message)
                .isEqualTo("User logged in");

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
                .isEqualTo("User registered");
    }

    @Test
    @Order(4)
    @DisplayName("User login")
    void testUserLogin_whenValidUserProvided_returnsUser() {
        AuthenticationRequest request = new AuthenticationRequest(EMAIL, PASSWORD);

        ResponseDto<AuthenticationResponse> response =
                given()
                        .body(request)
                        .when()
                        .post("/users/login")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract().response().as(new TypeRef<>() {
                        });

        userToken = response.data().token();
        assertNotNull(userToken);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseDto::message)
                .isEqualTo("User logged in");

        String usernameOrEmail = service.extractUsernameOrEmail(userToken);
        assertEquals(USERNAME, usernameOrEmail);
        userUuid = UUID.fromString(service.extractId(userToken));
        assertNotNull(userUuid);
    }

    @Test
    @Order(5)
    @DisplayName("Update User")
    void testUpdateUser_whenAuthenticatedAndValidUpdateUserProvided_returnsUpdatedUser() {
        UpdateUserRequest updateUser = new UpdateUserRequest(userUuid, "NewUsername", EMAIL);

        ResponseDto<UpdateUserRequest> response = given()
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
                .isEqualTo("User updated");

    }

    @Test
    @Order(6)
    @DisplayName("Admin can call update user")
    void testUpdateUser_whenAuthenticatedAdminAndValidUpdateUserProvided_returnsUpdatedUser() {
        UpdateUserRequest updateUser = new UpdateUserRequest(userUuid, USERNAME, EMAIL);

        ResponseDto<UpdateUserRequest> response = given()
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
                .isEqualTo("User updated");
    }

    @Test
    @Order(7)
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

        UserDto userDto = response.data();
        assertThat(userDto)
                .isNotNull()
                .extracting(UserDto::username)
                .isEqualTo(USERNAME);
    }

    @Test
    @Order(8)
    @DisplayName("Admin can delete user")
    void testDeleteUser_whenAuthenticatedAdminAndValidUuidProvided_returnsDeletedUser() {
        ResponseDto<UserDto> response = given()
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
                .isEqualTo("User deleted");

        UserDto userDto = response.data();
        assertThat(userDto)
                .isNotNull()
                .extracting(UserDto::available).isEqualTo(false);
    }
}
