package org.csc.chessclub.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.csc.chessclub.dto.CreateEventDto;
import jakarta.validation.Validator;
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
    void testValidationEventCreateDto_whenTitleIsNull_thenValidationFails() {
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
}
