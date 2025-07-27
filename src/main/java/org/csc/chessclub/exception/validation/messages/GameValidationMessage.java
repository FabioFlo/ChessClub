package org.csc.chessclub.exception.validation.messages;

import lombok.Getter;

@Getter
public final class GameValidationMessage {

  private GameValidationMessage() {
  }

  public static final String PGN_MUST_NOT_BE_BLANK = "Pgn must not be blank";
  public static final String PLAYER_NAME_TOO_LONG = "Player name is too long";
  public static final String PLAYER_NAME_NULL = "Player name is null";
}
