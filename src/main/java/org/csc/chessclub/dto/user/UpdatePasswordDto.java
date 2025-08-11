package org.csc.chessclub.dto.user;

import java.util.UUID;
import org.csc.chessclub.exception.validation.password.ValidPassword;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;

public record UpdatePasswordDto(@ValidUUID UUID uuid, @ValidPassword String password) {}
