package org.csc.chessclub.validator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.UUID;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.exception.validation.messages.GameValidationMessage;
import org.csc.chessclub.exception.validation.result.ResultValidationMessage;
import org.csc.chessclub.model.game.GameConstraints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameValidationTest extends BaseValidatorConfig {

  private final Validator validator = getValidator();
  private String propertyPath;
  private CreateGameDto createGameDto;

  @Test
  @DisplayName("Validation - Pgn is blank")
  void testValidCreateGameDto_whenPngIsBlank_thenValidationFails() {
    createGameDto = new CreateGameDto("", "", "", Result.BlackWon, null);
    propertyPath = "pgn";

    Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(GameValidationMessage.PGN_MUST_NOT_BE_BLANK));
  }

  @Test
  @DisplayName("Validation - Player name too long")
  void testValidCreateGameDto_whenPlayerNameTooLong_thenValidationFails() {
    String repeatedChar = "a";
    int length = GameConstraints.PLAYER_MAX_LENGTH + 1;
    String invalidLength = repeatedChar.repeat(length);

    propertyPath = "whitePlayerName";
    createGameDto = new CreateGameDto(invalidLength, "", "game pgn", Result.BlackWon, null);

    Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(GameValidationMessage.PLAYER_NAME_TOO_LONG));
  }

  @Test
  @DisplayName("Validation - Player name is null")
  void testValidCreateGameDto_whenPlayerNameIsNull_thenValidationFails() {
    propertyPath = "whitePlayerName";
    createGameDto = new CreateGameDto(null, "", "game pgn", Result.BlackWon, null);

    Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(GameValidationMessage.PLAYER_NAME_NULL));
  }

  @Test
  @DisplayName("Validation - Result is not valid")
  void testValidCreateGameDto_whenResultIsNotValid_thenValidationFails() {
    createGameDto = new CreateGameDto("", "", "game pgn", null, null);
    String propertyPath = "result";

    Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(ResultValidationMessage.RESULT_MUST_BE_VALID));
  }

  @Test
  @DisplayName("Valid CreateGameDto")
  void testValidCreateGameDto_whenValidCreateGameDto_thenSuccess() {
    createGameDto = new CreateGameDto("", "", "game pgn", Result.BlackWon, null);

    Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
    assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("Valid UpdateGameDto")
  void testValidUpdateGameDto_whenValidUpdateGameDto_thenSuccess() {
    UpdateGameDto updateGameDto =
        new UpdateGameDto(UUID.randomUUID(), "", "", "game pgn", Result.BlackWon, null);

    Set<ConstraintViolation<UpdateGameDto>> violations = validator.validate(updateGameDto);
    assertThat(violations).isEmpty();
  }
}
