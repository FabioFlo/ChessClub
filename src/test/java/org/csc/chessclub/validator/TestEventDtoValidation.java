package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.csc.chessclub.dto.CreateEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TestEventDtoValidation {

    private Validator validator;

    CreateEventDto eventDto;
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_AUTHOR_LENGTH = 100;
    private static final int MIN_TITLE_LENGTH = 2;
    private static final int MIN_AUTHOR_LENGTH = 2;
    private static final String TITLE = "Test Title";
    private static final String DESCRIPTION = "Test Description";
    private static final String AUTHOR = "Test Author";
    private static final String ANNOUNCEMENT_PDF = "Test Announcement PDF";
    private static final String INVALID_STRING_LENGTH = "A";


    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    @DisplayName("Validation Event Create Dto - Title is blank")
    void testValidationEventCreateDto_whenTitleIsBlank_thenValidationFails() {
        eventDto = new CreateEventDto(null, DESCRIPTION, AUTHOR, ANNOUNCEMENT_PDF);

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("title") &&
                        v.getMessage().equals("Title must not be blank"));
    }

    @Test
    @DisplayName("Validation Event Create Dto - Description is blank")
    void testValidationEventCreateDto_whenDescriptionIsBlank_thenValidationFails() {
        eventDto = new CreateEventDto(TITLE, null, AUTHOR, ANNOUNCEMENT_PDF);

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("description") &&
                        v.getMessage().equals("Description must not be blank"));
    }

    @Test
    @DisplayName("Validation Event Create Dto - Author is blank")
    void testValidationEventCreateDto_whenAuthorIsBlank_thenValidationFails() {
        eventDto = new CreateEventDto(TITLE, DESCRIPTION, null, ANNOUNCEMENT_PDF);

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("author") &&
                        v.getMessage().equals("Author must not be blank"));
    }

    @Test
    @DisplayName("Validation Event Create - Title size not valid")
    void testValidationEventCreateDto_whenTitleSizeNotValid_thenValidationFails() {
        CreateEventDto eventDto = new CreateEventDto(
                INVALID_STRING_LENGTH, DESCRIPTION, AUTHOR, ANNOUNCEMENT_PDF);

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("title") &&
                        v.getMessage().equals("Title must be between "
                                + MIN_TITLE_LENGTH + " and "
                                + MAX_TITLE_LENGTH + " characters"));
    }

    @Test
    @DisplayName("Validation Event Create - Author size not valid")
    void testValidationEventCreateDto_whenAuthorSizeNotValid_thenValidationFails() {
        CreateEventDto eventDto = new CreateEventDto(
                TITLE, DESCRIPTION, INVALID_STRING_LENGTH, ANNOUNCEMENT_PDF);

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("author") &&
                        v.getMessage().equals("Author must be between "
                                + MIN_AUTHOR_LENGTH + " and "
                                + MAX_AUTHOR_LENGTH + " characters"));
    }
}
