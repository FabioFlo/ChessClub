package org.csc.chessclub.dto.game;

import org.csc.chessclub.enums.Result;

public record CreateGameDto(
        String whitePlayerName,
        String blackPlayerName,
        String pgn,
        Result result
) {
}
