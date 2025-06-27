package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.RandomStringUtils;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.exception.validation.messages.GameValidationMessage;
import org.csc.chessclub.exception.validation.result.ResultValidationMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class GameValidationTest extends BaseValidatorConfig {

    private final Validator validator = getValidator();
    private String propertyPath = "";
    CreateGameDto createGameDto;

    @Test
    @DisplayName("Validation - Pgn is blank")
    void testValidCreateGameDto_whenPngIsBlank_thenValidationFails() {
        createGameDto = new CreateGameDto("", "", "", Result.BlackWon);
        propertyPath = "pgn";

        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(GameValidationMessage.PGN_MUST_NOT_BE_BLANK));
    }

    @Test
    @DisplayName("Validation - Player name too long")
    @SuppressWarnings(value = "deprecation")
    void testValidCreateGameDto_whenPlayerNameTooLong_thenValidationFails() {
        String generated = RandomStringUtils.randomAlphanumeric(30);
        propertyPath = "whitePlayerName";
        createGameDto = new CreateGameDto(generated, "", "game pgn", Result.BlackWon);

        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(GameValidationMessage.PLAYER_NAME_TOO_LONG));
    }

    @Test
    @DisplayName("Validation - Player name is null")
    void testValidCreateGameDto_whenPlayerNameIsNull_thenValidationFails() {
        propertyPath = "whitePlayerName";
        createGameDto = new CreateGameDto(null, "", "game pgn", Result.BlackWon);

        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(GameValidationMessage.PLAYER_NAME_NULL));
    }

    @Test
    @DisplayName("Validation - Result is not valid")
    void testValidCreateGameDto_whenResultIsNotValid_thenValidationFails() {
        createGameDto = new CreateGameDto("", "", "game pgn", null);
        String propertyPath = "result";

        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(ResultValidationMessage.RESULT_MUST_BE_VALID));
    }

    @Test
    @DisplayName("Valid CreateGameDto")
    void testValidCreateGameDto_whenValidCreateGameDto_thenSuccess() {
        createGameDto = new CreateGameDto("", "", "game pgn", Result.BlackWon);

        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Valid UpdateGameDto")
    void testValidUpdateGameDto_whenValidUpdateGameDto_thenSuccess() {
        UpdateGameDto updateGameDto = new UpdateGameDto(UUID.randomUUID(), "", "", "game pgn", Result.BlackWon);

        Set<ConstraintViolation<UpdateGameDto>> violations = validator.validate(updateGameDto);
        assertThat(violations).isEmpty();
    }
}
