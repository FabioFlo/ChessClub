package org.csc.chessclub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.csc.chessclub.exception.validation.messages.UserValidationMessage;
import org.csc.chessclub.exception.validation.password.ValidPassword;

public record RegisterUserRequest(
        @NotBlank(message = UserValidationMessage.USERNAME_MUST_NOT_BE_BLANK)
        String username,
        @Email(message = UserValidationMessage.EMAIL_MUST_BE_VALID)
        String email,
        @ValidPassword
        String password
) {
}
