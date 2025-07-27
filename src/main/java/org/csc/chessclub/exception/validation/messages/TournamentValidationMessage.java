package org.csc.chessclub.exception.validation.messages;

import org.csc.chessclub.model.tournament.TournamentConstraints;

public final class TournamentValidationMessage {

  private TournamentValidationMessage() {
  }

  public static final String TITLE_MUST_NOT_BE_BLANK = "Title must not be blank";
  public static final String DESCRIPTION_MUST_NOT_BE_BLANK = "Description must not be blank";
  public static final String DATE_MUST_NOT_BE_NULL = "Date must not be null";
  public static final String TITLE_LENGTH_REQUIRED = "Title should be between "
      + TournamentConstraints.TITLE_MIN_LENGTH
      + " and "
      + TournamentConstraints.TITLE_MAX_LENGTH + " characters";
  public static final String DESCRIPTION_LENGTH_REQUIRED = "Description should be between "
      + TournamentConstraints.DESCRIPTION_MIN_LENGTH
      + " and "
      + TournamentConstraints.DESCRIPTION_MAX_LENGTH + " characters";
}
