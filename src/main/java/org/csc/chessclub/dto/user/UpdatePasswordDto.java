package org.csc.chessclub.dto.user;

import org.csc.chessclub.exception.validation.password.ValidPassword;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;

import java.util.UUID;

public record UpdatePasswordDto(
        @ValidUUID
        UUID uuid,
        @ValidPassword
        String password) {
}
