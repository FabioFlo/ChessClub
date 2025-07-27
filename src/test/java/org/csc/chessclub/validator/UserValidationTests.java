package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.exception.validation.messages.UserValidationMessage;
import org.csc.chessclub.exception.validation.password.PasswordValidationMassage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserValidationTests extends BaseValidatorConfig {

  private final Validator validator = getValidator();

  private static final String USERNAME = "Test Username";
  private static final String PASSWORD = "Password1_";
  private static final String EMAIL = "email@email.com";
  private String propertyPath;

  @Test
  @DisplayName("Validation Register User Request - Username is blank")
  void testValidationRegisterUserRequest_whenUsernameIsBlank_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(null, PASSWORD, EMAIL);
    propertyPath = "username";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage().equals(UserValidationMessage.USERNAME_MUST_NOT_BE_BLANK));
  }

  @Test
  @DisplayName("Validation Register User Request - Password too short")
  void testValidationRegisterUserRequest_whenPasswordTooShort_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, "");
    propertyPath = "password";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage().equals(PasswordValidationMassage.PASSWORD_TOO_SHORT));
  }

  @Test
  @DisplayName("Validation Register User Request - Password null")
  void testValidationRegisterUserRequest_whenPasswordNull_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, null);
    propertyPath = "password";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage().equals(PasswordValidationMassage.PASSWORD_NOT_VALID));
  }

  @Test
  @DisplayName("Validation Register User Request - Password not contains upper case")
  void testValidationRegisterUserRequest_whenPasswordNotContainsUpperCase_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, "password");
    propertyPath = "password";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage()
                .equals(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_UPPERCASE_LETTER));
  }

  @Test
  @DisplayName("Validation Register User Request - Password not contains lower case")
  void testValidationRegisterUserRequest_whenPasswordNotContainsLowerCase_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, "PASSWORD");
    propertyPath = "password";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage()
                .equals(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_LOWERCASE_LETTER));
  }

  @Test
  @DisplayName("Validation Register User Request - Password not contains digit")
  void testValidationRegisterUserRequest_whenPasswordNotContainsDigit_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, "PassworD");
    propertyPath = "password";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage().equals(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_DIGIT));
  }

  @Test
  @DisplayName("Validation Register User Request - Password not contains special character")
  void testValidationRegisterUserRequest_whenPasswordNotContainsSpecialCharacter_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, "PassworD");
    propertyPath = "password";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage()
                .equals(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_SPECIAL_CHARACTER));
  }

  @Test
  @DisplayName("Validation Register User Request - Password contains whitespace")
  void testValidationRegisterUserRequest_whenPasswordContainsWhitespace_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, "PassworD ");
    propertyPath = "password";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage().equals(PasswordValidationMassage.PASSWORD_MUST_NOT_CONTAIN_WHITESPACE));
  }

  @Test
  @DisplayName("Validation Register User Request - Password valid")
  void testValidationRegisterUserRequest_whenPasswordValid_thenValidationSucceeds() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL,
        "<Password1>");

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);
    assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("Validation Register User Request - Email invalid")
  void testValidationRegisterUserRequest_whenEmailInvalid_thenValidationFails() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, "invalid-email",
        PASSWORD);
    propertyPath = "email";

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);

    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals(propertyPath) &&
            v.getMessage().equals(UserValidationMessage.EMAIL_MUST_BE_VALID));
  }

  @Test
  void testValidationUpdateUserRequest_whenValidUserProvided_thenValidationSucceeds() {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);

    Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(
        registerUserRequest);
    assertThat(violations).isEmpty();
  }
}
