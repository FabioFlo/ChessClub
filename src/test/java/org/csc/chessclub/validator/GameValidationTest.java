package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.exception.validation.messages.GameValidationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class GameValidationTest {
    //TODO: be sure that the result is correctly set with the enum value if "0-1" is passed for example

    private Validator validator;

    CreateGameDto createGameDto;

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Validation CreateGame - Pgn is blank")
    void testValidCreateGameDto_whenPngIsBlank_thenValidationFails() {
        createGameDto = new CreateGameDto("", "", "", Result.BlackWon);

        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(createGameDto);
       assertThat(violations).anyMatch(v ->
               v.getPropertyPath().toString().equals("pgn") &&
                       v.getMessage().equals(GameValidationMessage.PGN_MUST_NOT_BE_BLANK));
    }
}
