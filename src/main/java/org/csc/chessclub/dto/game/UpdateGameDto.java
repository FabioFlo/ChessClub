package org.csc.chessclub.dto.game;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.exception.validation.messages.GameValidationMessage;
import org.csc.chessclub.exception.validation.result.ValidResult;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.model.game.GameConstraints;

import java.util.UUID;

public record UpdateGameDto(
        @ValidUUID
        UUID uuid,
        @Size(max = GameConstraints.PLAYER_MAX_LENGTH, message = GameValidationMessage.PLAYER_NAME_TOO_LONG)
        @NotNull(message = GameValidationMessage.PLAYER_NAME_NULL)
        String whitePlayerName,
        @Size(max = GameConstraints.PLAYER_MAX_LENGTH, message = GameValidationMessage.PLAYER_NAME_TOO_LONG)
        @NotNull(message = GameValidationMessage.PLAYER_NAME_NULL)
        String blackPlayerName,
        @NotNull(message = GameValidationMessage.PGN_MUST_NOT_BE_BLANK)
        String pgn,
        @ValidResult
        Result result,
        UUID tournamentId

) {
}
