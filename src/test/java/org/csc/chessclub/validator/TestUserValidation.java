package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.exception.validation.UserValidationMessage;
import org.csc.chessclub.exception.validation.password.PasswordValidationMassage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TestUserValidation {

    private Validator validator;

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@email.com";

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Validation Register User Request - Username is blank")
    void testValidationRegisterUserRequest_whenUsernameIsBlank_thenValidationFails() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(null, PASSWORD, EMAIL);

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(registerUserRequest);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("username") &&
                        v.getMessage().equals(UserValidationMessage.USERNAME_MUST_NOT_BE_BLANK));
    }

    @Test
    @DisplayName("Validation Register User Request - Password too short")
    void testValidationRegisterUserRequest_whenPasswordTooShort_thenValidationFails() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, "");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(registerUserRequest);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().equals(PasswordValidationMassage.PASSWORD_TOO_SHORT));
    }

    @Test
    @DisplayName("Validation Register User Request - Password null")
    void testValidationRegisterUserRequest_whenPasswordNull_thenValidationFails() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, null);

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(registerUserRequest);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().equals(PasswordValidationMassage.PASSWORD_NOT_VALID));
    }

    @Test
    @DisplayName("Validation Register User Request - Password not contains upper case")
    void testValidationRegisterUserRequest_whenPasswordNotContainsUpperCase_thenValidationFails() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(registerUserRequest);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().equals(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_UPPERCASE_LETTER));
    }
}
