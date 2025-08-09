package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.exception.validation.messages.TournamentValidationMessage;
import org.csc.chessclub.exception.validation.uuid.UuidValidationMessage;
import org.csc.chessclub.model.tournament.TournamentConstraints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TournamentValidationTests extends BaseValidatorConfig {

    private final Validator validator = getValidator();

    private CreateTournamentDto createTournamentDto;
    private UpdateTournamentDto updateTournamentDto;
    private static final String TITLE = "Tournament";
    private static final String DESCRIPTION = "Description";
    private static final LocalDate START_DATE = LocalDate.parse("2019-01-01");
    private static final LocalDate END_DATE = LocalDate.parse("2018-01-03");
    private static final UUID EVENT_ID = UUID.randomUUID();
    private String propertyPath;

    @Test
    @DisplayName("Validation CreateTournamentDto - Title is blank")
    void testValidationCreateTournamentDto_whenTitleIsBlank_thenValidationFails() {
        createTournamentDto = new CreateTournamentDto(
                "", START_DATE, END_DATE, DESCRIPTION, EVENT_ID);

        propertyPath = "title";
        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(
                createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(TournamentValidationMessage.TITLE_MUST_NOT_BE_BLANK));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Description is blank")
    void testValidationCreateTournamentDto_whenDescriptionIsBlank_thenValidationFails() {
        createTournamentDto = new CreateTournamentDto(TITLE, START_DATE, END_DATE, "", EVENT_ID);

        propertyPath = "description";
        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(
                createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(TournamentValidationMessage.DESCRIPTION_MUST_NOT_BE_BLANK));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Date is null")
    void testValidationCreateTournamentDto_whenDateIsNull_thenValidationFails() {
        createTournamentDto = new CreateTournamentDto(TITLE, null, END_DATE, DESCRIPTION, null);

        propertyPath = "startDate";
        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(
                createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(TournamentValidationMessage.DATE_MUST_NOT_BE_NULL));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Title size too short")
    void testValidationCreateTournamentDto_whenTitleSizeTooShort_thenValidationFails() {
        String repeatedChar = "a";
        int length = TournamentConstraints.TITLE_MIN_LENGTH - 1;
        String tooShortTitle = repeatedChar.repeat(length);

        propertyPath = "title";
        createTournamentDto = new CreateTournamentDto(tooShortTitle, START_DATE, END_DATE, DESCRIPTION,
                null);

        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(
                createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(TournamentValidationMessage.TITLE_LENGTH_REQUIRED));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Title size too long")
    void testValidationCreateTournamentDto_whenTitleSizeTooLong_thenValidationFails() {
        String repeatedChar = "a";
        int length = TournamentConstraints.TITLE_MAX_LENGTH + 1;
        String tooLongTitle = repeatedChar.repeat(length);

        propertyPath = "title";
        createTournamentDto = new CreateTournamentDto(tooLongTitle, START_DATE, END_DATE, DESCRIPTION,
                null);

        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(
                createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(TournamentValidationMessage.TITLE_LENGTH_REQUIRED));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Description size too long")
    void testValidationCreateTournamentDto_whenDescriptionSizeTooLong_thenValidationFails() {
        String repeatedChar = "a";
        int length = TournamentConstraints.DESCRIPTION_MAX_LENGTH + 1;
        String tooLongDescription = repeatedChar.repeat(length);

        propertyPath = "description";
        createTournamentDto = new CreateTournamentDto(TITLE, START_DATE, END_DATE, tooLongDescription,
                null);

        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(
                createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(TournamentValidationMessage.DESCRIPTION_LENGTH_REQUIRED));
    }

    @Test
    @DisplayName("Validation CreateTournamentDto - Description size too short")
    void testValidationCreateTournamentDto_whenDescriptionSizeTooShort_thenValidationFails() {
        String repeatedChar = "a";
        int length = TournamentConstraints.DESCRIPTION_MIN_LENGTH - 1;
        String tooShortDescription = repeatedChar.repeat(length);

        propertyPath = "description";
        createTournamentDto = new CreateTournamentDto(TITLE, START_DATE, END_DATE, tooShortDescription,
                null);

        Set<ConstraintViolation<CreateTournamentDto>> violations = validator.validate(
                createTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(TournamentValidationMessage.DESCRIPTION_LENGTH_REQUIRED));
    }

    @Test
    @DisplayName("Validation UpdateTournamentDto - Uuid null or not valid")
    void testValidationUpdateTournamentDto_whenUUIDNotValid_thenValidationFails() {
        updateTournamentDto = new UpdateTournamentDto(null, TITLE, START_DATE, END_DATE, DESCRIPTION,
                null);

        propertyPath = "uuid";
        Set<ConstraintViolation<UpdateTournamentDto>> violations = validator.validate(
                updateTournamentDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals(propertyPath) &&
                        v.getMessage().equals(UuidValidationMessage.UUID_MUST_BE_VALID));
    }

    @Test
    @DisplayName("Validation UpdateTournamentDto - Is valid")
    void testValidationUpdateTournamentDto_whenValidUpdateDtoProvided_thenValidationPasses() {
        updateTournamentDto = new UpdateTournamentDto(UUID.randomUUID(), TITLE, START_DATE, END_DATE,
                DESCRIPTION, null);

        Set<ConstraintViolation<UpdateTournamentDto>> violations = validator.validate(
                updateTournamentDto);
        assertTrue(violations.isEmpty(), "Validation failed");
    }
}
