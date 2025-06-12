package org.csc.chessclub.dto.game;

import jakarta.validation.constraints.NotBlank;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.exception.validation.messages.GameValidationMessage;

public record CreateGameDto(
        String whitePlayerName,
        String blackPlayerName,
        @NotBlank(message = GameValidationMessage.PGN_MUST_NOT_BE_BLANK)
        String pgn,
        Result result
) {
}
