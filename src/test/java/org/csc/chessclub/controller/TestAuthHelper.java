package org.csc.chessclub.controller;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.auth.AuthenticationResponse;
import org.csc.chessclub.dto.ResponseDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@TestConfiguration
public class TestAuthHelper {
  public ResponseDto<AuthenticationResponse> loginUser(String username, String password) {
    return given()
        .body(new AuthenticationRequest(username, password))
        .when()
        .post("/users/login")
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .response()
        .as(new TypeRef<>() {});
  }

  public String getTokenForUser(String username, String password) {
    return loginUser(username, password).data().token();
  }

  public RequestSpecification withBearerToken(String token) {
    return given().header("Authorization", "Bearer " + token);
  }
}
