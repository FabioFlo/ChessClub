package org.csc.chessclub.dto;

import java.util.Optional;

public record ResponseDto(
        String message,
        boolean success) {
}
