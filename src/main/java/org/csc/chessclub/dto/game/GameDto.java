package org.csc.chessclub.dto.game;

import java.util.UUID;
import org.csc.chessclub.enums.Result;

public record GameDto(
    UUID uuid, String whitePlayerName, String blackPlayerName, String pgn, Result result) {}
