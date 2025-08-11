package org.csc.chessclub.controller;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.UUID;
import org.csc.chessclub.TestcontainersConfiguration;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.repository.GameRepository;
import org.csc.chessclub.repository.TournamentRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, TestDataFactory.class, TestAuthHelper.class})
public abstract class BaseTestConfiguration {

  @LocalServerPort private int port;

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

  @Autowired protected UserRepository userRepository;
  @Autowired protected TestDataFactory dataFactory;
  @Autowired protected TestAuthHelper authHelper;
  @Autowired private EventRepository eventRepository;
  @Autowired private TournamentRepository tournamentRepository;
  @Autowired private GameRepository gameRepository;

  @BeforeAll
  void setup() {
    configureRestAssured();
    cleanDatabase();
    createBaseTestData();
  }

  private void configureRestAssured() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    RestAssured.requestSpecification =
        new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();
  }

  private void cleanDatabase() {
    gameRepository.deleteAll();
    tournamentRepository.deleteAll();
    eventRepository.deleteAll();
    userRepository.deleteAll();
  }

  protected void createBaseTestData() {
    baseAdminUser =
        dataFactory.createUser(
            UserEntity.builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(adminPassword)
                .role(Role.ADMIN)
                .available(true)
                .build());

    baseUser =
        dataFactory.createUser(
            UserEntity.builder()
                .username(userUsername)
                .email(userEmail)
                .password(userPassword)
                .role(Role.USER)
                .available(true)
                .build());
  }

  protected RequestSpecification withPageable(Pageable pageable) {
    RequestSpecification spec = given();
    if (pageable != null) {
      spec.queryParam("page", pageable.getPageNumber()).queryParam("size", pageable.getPageSize());
      pageable
          .getSort()
          .forEach(
              order -> spec.queryParam("sort", order.getProperty() + "," + order.getDirection()));
    }
    return spec;
  }

  protected UserEntity getBaseAdmin() {
    return baseAdminUser;
  }

  protected UUID getBaseUserUuid() {
    return baseUser.getUuid();
  }

  protected String getUserToken() {
    return authHelper.getTokenForUser(userUsername, userPassword);
  }
}
