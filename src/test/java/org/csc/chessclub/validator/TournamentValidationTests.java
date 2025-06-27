package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.exception.validation.messages.TournamentValidationMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TournamentValidationTests extends BaseValidatorConfig {

    private final Validator validator = getValidator();

    private CreateTournamentDto createTournamentDto;
    private static final String TITLE = "Tournament";
    private static final String DESCRIPTION = "Description";
    private static final LocalDate START_DATE = LocalDate.parse("2019-01-01");
    private static final LocalDate END_DATE = LocalDate.parse("2018-01-03");
    private static final UUID EVENT_ID = UUID.randomUUID();

    @Test
    @DisplayName("Validation CreateTournamentDto - Title is blank")
    void testValidationCreateTournamentDto_whenTitleIsBlank_thenValidationFails() {
        createTournamentDto = new CreateTournamentDto(
                "", START_DATE, END_DATE, DESCRIPTION, EVENT_ID);

        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("title") &&
                        v.getMessage().equals(TournamentValidationMessage.TITLE_MUST_NOT_BE_BLANK));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Description is blank")
    void testValidationCreateTournamentDto_whenDescriptionIsBlank_thenValidationFails() {
        createTournamentDto = new CreateTournamentDto(TITLE, START_DATE, END_DATE, "", EVENT_ID);

        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("description") &&
                        v.getMessage().equals(TournamentValidationMessage.DESCRIPTION_MUST_NOT_BE_BLANK));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Date is null")
    void testValidationCreateTournamentDto_whenDateIsNull_thenValidationFails() {
        createTournamentDto = new CreateTournamentDto(TITLE, null, END_DATE, DESCRIPTION, null);

        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("startDate") &&
                        v.getMessage().equals(TournamentValidationMessage.DATE_MUST_NOT_BE_NULL));
    }

}
