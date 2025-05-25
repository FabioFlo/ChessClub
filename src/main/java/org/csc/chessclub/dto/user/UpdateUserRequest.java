package org.csc.chessclub.dto.user;

import org.csc.chessclub.enums.Role;

import java.util.UUID;

public record UpdateUserRequest(
        UUID uuid,
        String username,
        String email,
        Role role
) {
}
