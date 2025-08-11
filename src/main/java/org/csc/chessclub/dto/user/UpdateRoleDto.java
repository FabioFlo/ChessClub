package org.csc.chessclub.dto.user;

import java.util.UUID;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.exception.validation.role.ValidRole;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;

public record UpdateRoleDto(
    @ValidUUID UUID uuid, @ValidRole Role oldRole, @ValidRole Role newRole) {}
