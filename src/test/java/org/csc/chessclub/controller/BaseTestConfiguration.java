package org.csc.chessclub.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.csc.chessclub.TestcontainersConfiguration;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.auth.AuthenticationResponse;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public abstract class BaseTestConfiguration {

  @LocalServerPort
  private int port;

  private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
  private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

  @Value("${admin.username}")
  private String adminUsername;
  @Value("${admin.password}")
  private String adminPassword;
  @Value("${admin.email}")
  private String adminEmail;
  private UserEntity baseAdminUser;

  @Value("${user.username}")
  private String userUsername;
  @Value("${user.password}")
  private String userPassword;
  @Value("${user.email}")
  private String userEmail;
  private UserEntity baseUser;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeAll
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;

    RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

    RestAssured.requestSpecification = new RequestSpecBuilder()
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();

    userRepository.deleteAll();

   baseAdminUser =  userRepository.save(UserEntity.
        builder()
        .username(adminUsername)
        .email(adminEmail)
        .password(passwordEncoder.encode(adminPassword))
        .role(Role.ADMIN)
        .available(true).build());

   baseUser = userRepository.save(UserEntity
        .builder()
        .username(userUsername)
        .email(userEmail)
        .password(passwordEncoder.encode(userPassword))
        .role(Role.USER)
        .available(true)
        .build());
  }


  protected RequestSpecification withPageable(Pageable pageable) {
    RequestSpecification spec = given();
    if (pageable != null) {
      spec
          .queryParam("page", pageable.getPageNumber())
          .queryParam("size", pageable.getPageSize());
      pageable.getSort().forEach(order ->
          spec.queryParam("sort", order.getProperty() + "," + order.getDirection())
      );
    }
    return spec;
  }

    protected UserEntity getBaseAdmin() {
        return baseAdminUser;
    }

    protected UserEntity getBaseUser() {
        return baseUser;
    }

    protected UUID getBaseAdminUuid() {
        return baseAdminUser.getUuid();
    }

    protected UUID getBaseUserUuid() {
        return baseUser.getUuid();
    }

    protected String getAdminToken() {
        return getTokenForUser(adminUsername, adminPassword);
    }

    protected String getUserToken() {
        return getTokenForUser(userUsername, userPassword);
    }


    public UserEntity createUser(UserEntity user) {
        return userRepository.save(UserEntity.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(user.getRole())
                .available(user.isAvailable())
                .build());
    }

    public UserEntity createUnavailableUser() {
        return createUser(
                UserEntity.builder()
                        .username("unavailable")
                        .email("unabailable@email.com")
                        .password(passwordEncoder.encode("Test123_"))
                        .role(Role.USER)
                        .available(false)
                        .build());
    }

    public ResponseDto<AuthenticationResponse> loginUser(String username, String password) {
        return given()
                .body(new AuthenticationRequest(username, password))
                .when()
                .post("/users/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().as(new TypeRef<>() {
                });
    }

    public String getTokenForUser(String username, String password) {
        return loginUser(username, password).data().token();
    }

    public RequestSpecification withBearerToken(String token) {
        return given().header("Authorization", "Bearer " + token);
    }
}
