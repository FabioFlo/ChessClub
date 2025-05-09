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

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Validation Event Create Dto - Title is blank")
    void testValidationEventCreateDto_whenTitleIsBlank_thenValidationFails() {
        CreateEventDto eventDto = new CreateEventDto(
                null,
                "Test Description",
                "Test Author",
                "Test Announcement PDF");

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("title") &&
                        v.getMessage().equals("Title must not be blank"));
    }

    @Test
    @DisplayName("Validation Event Create Dto - Description is blank")
    void testValidationEventCreateDto_whenDescriptionIsBlank_thenValidationFails() {
        CreateEventDto eventDto = new CreateEventDto(
                "Test Title",
                null,
                "Test Author",
                "Test Announcement PDF");

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("description") &&
                        v.getMessage().equals("Description must not be blank"));
    }

    @Test
    @DisplayName("Validation Event Create Dto - Author is blank")
    void testValidationEventCreateDto_whenAuthorIsBlank_thenValidationFails() {
        CreateEventDto eventDto = new CreateEventDto(
                "Test Title",
                "Test Description",
                null,
                "Test Announcement PDF");

        Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("author") &&
                        v.getMessage().equals("Author must not be blank"));
    }

}
