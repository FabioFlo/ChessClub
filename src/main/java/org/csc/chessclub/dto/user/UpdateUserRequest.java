package org.csc.chessclub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.csc.chessclub.exception.validation.UserValidationMessage;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;

import java.util.UUID;

public record UpdateUserRequest(
        @ValidUUID
        UUID uuid,
        @NotBlank(message = UserValidationMessage.USERNAME_MUST_NOT_BE_BLANK)
        String username,
        @Email
        String email
) {
}
