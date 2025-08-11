package org.csc.chessclub.validator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.UUID;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.exception.validation.messages.EventValidationMessage;
import org.csc.chessclub.exception.validation.uuid.UuidValidationMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EventValidationTests extends BaseValidatorConfig {

  private final Validator validator = getValidator();

  private CreateEventDto createEvent;
  private static final String TITLE = "Test Title";
  private static final String DESCRIPTION = "Test Description";
  private static final String AUTHOR = "Test Author";
  private static final String INVALID_STRING_LENGTH = "A";
  private final UUID uuid = UUID.randomUUID();
  private String propertyPath;

  @Test
  @DisplayName("Validation Event Create Dto - Title is blank")
  void testValidationEventCreateDto_whenTitleIsBlank_thenValidationFails() {
    createEvent = new CreateEventDto(null, DESCRIPTION, AUTHOR);
    propertyPath = "title";

    Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(createEvent);

    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(EventValidationMessage.TITLE_MUST_NOT_BE_BLANK));
  }

  @Test
  @DisplayName("Validation Event Create Dto - Description is blank")
  void testValidationEventCreateDto_whenDescriptionIsBlank_thenValidationFails() {
    createEvent = new CreateEventDto(TITLE, null, AUTHOR);
    propertyPath = "description";

    Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(createEvent);

    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(EventValidationMessage.DESCRIPTION_MUST_NOT_BE_BLANK));
  }

  @Test
  @DisplayName("Validation Event Create Dto - Author is blank")
  void testValidationEventCreateDto_whenAuthorIsBlank_thenValidationFails() {
    createEvent = new CreateEventDto(TITLE, DESCRIPTION, null);
    propertyPath = "author";

    Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(createEvent);

    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(EventValidationMessage.AUTHOR_MUST_NOT_BE_BLANK));
  }

  @Test
  @DisplayName("Validation Event Create - Title size not valid")
  void testValidationEventCreateDto_whenTitleSizeNotValid_thenValidationFails() {
    CreateEventDto eventDto = new CreateEventDto(INVALID_STRING_LENGTH, DESCRIPTION, AUTHOR);
    propertyPath = "title";

    Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);

    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(EventValidationMessage.TITLE_LENGTH_REQUIRED));
  }

  @Test
  @DisplayName("Validation Event Create - Author size not valid")
  void testValidationEventCreateDto_whenAuthorSizeNotValid_thenValidationFails() {
    CreateEventDto eventDto = new CreateEventDto(TITLE, DESCRIPTION, INVALID_STRING_LENGTH);
    propertyPath = "author";

    Set<ConstraintViolation<CreateEventDto>> violations = validator.validate(eventDto);

    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(EventValidationMessage.AUTHOR_LENGTH_REQUIRED));
  }

  @Test
  @DisplayName("Validation Event Update - Is valid")
  void testValidationEventUpdateDto_whenValidEventDetailsProvided_thenValidationSucceeds() {
    UpdateEventDto eventDetails = new UpdateEventDto(uuid, "New test title", DESCRIPTION, AUTHOR);

    Set<ConstraintViolation<UpdateEventDto>> violations = validator.validate(eventDetails);
    assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("Validation Event Update - Uuid null or not valid")
  void testValidationEventUpdateDto_whenUuidNullOrNotValid_thenValidationFails() {
    UpdateEventDto eventDetails = new UpdateEventDto(null, "New test title", DESCRIPTION, AUTHOR);
    propertyPath = "uuid";

    Set<ConstraintViolation<UpdateEventDto>> violations = validator.validate(eventDetails);

    assertThat(violations)
        .anyMatch(
            v ->
                v.getPropertyPath().toString().equals(propertyPath)
                    && v.getMessage().equals(UuidValidationMessage.UUID_MUST_BE_VALID));
  }
}
