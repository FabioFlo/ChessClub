package org.csc.chessclub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.csc.chessclub.exception.validation.messages.UserValidationMessage;
import org.csc.chessclub.exception.validation.password.ValidPassword;
import org.csc.chessclub.model.user.UserConstraints;

public record RegisterUserRequest(
    @NotBlank(message = UserValidationMessage.USERNAME_MUST_NOT_BE_BLANK)
    @Size(min = UserConstraints.USERNAME_MIN_LENGTH,
        max = UserConstraints.USERNAME_MAX_LENGTH,
        message = UserValidationMessage.USERNAME_LENGTH_REQUIRED)
    String username,
    @Email(message = UserValidationMessage.EMAIL_MUST_BE_VALID)
    @Size(min = UserConstraints.EMAIL_MIN_LENGTH,
        max = UserConstraints.EMAIL_MAX_LENGTH,
        message = UserValidationMessage.EMAIL_LENGTH_REQUIRED)
    String email,
    @ValidPassword
    String password
) {

}
