package org.csc.chessclub.dto.user;

import java.util.UUID;
import org.csc.chessclub.enums.Role;

public record UserDto(UUID uuid, String username, String email, Role role, boolean available) {}
