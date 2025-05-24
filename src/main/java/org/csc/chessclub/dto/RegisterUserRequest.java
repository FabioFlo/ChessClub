package org.csc.chessclub.dto;

public record RegisterUserRequest(
        String username,
        String email,
        String password
) {
}
