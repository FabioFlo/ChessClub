package org.csc.chessclub.dto;

import org.csc.chessclub.enums.Role;

import java.util.UUID;

public record UpdateUserRequest(
        UUID uuid,
        String username,
        String email,
        Role role
) {
}
