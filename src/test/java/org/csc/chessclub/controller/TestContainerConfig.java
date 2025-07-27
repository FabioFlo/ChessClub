package org.csc.chessclub.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.csc.chessclub.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public abstract class TestContainerConfig {

  @LocalServerPort
  private int port;

  private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
  private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

  @BeforeAll
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;

    RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

    RestAssured.requestSpecification = new RequestSpecBuilder()
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .build();
  }
}
