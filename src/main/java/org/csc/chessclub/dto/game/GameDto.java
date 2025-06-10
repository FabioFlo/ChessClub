package org.csc.chessclub.dto.game;

import org.csc.chessclub.enums.Result;

import java.util.UUID;

public record GameDto(
        UUID uuid,
        String whitePlayerName,
        String blackPlayerName,
        String pgn,
        Result result
) {
}
