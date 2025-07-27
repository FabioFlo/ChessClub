package org.csc.chessclub.dto.user;

import java.util.UUID;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;

public record UpdateRoleDto(
    @ValidUUID
    UUID uuid,
    Role oldRole,
    Role newRole
) {

}
