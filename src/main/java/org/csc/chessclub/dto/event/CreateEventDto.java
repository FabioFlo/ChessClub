package org.csc.chessclub.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.csc.chessclub.exception.validation.messages.EventValidationMessage;
import org.csc.chessclub.model.event.EventConstraints;

public record CreateEventDto(
        @NotBlank(message = EventValidationMessage.TITLE_MUST_NOT_BE_BLANK)
        @Size(min = EventConstraints.TITLE_MIN_LENGTH,
                max = EventConstraints.TITLE_MAX_LENGTH,
                message = EventValidationMessage.TITLE_LENGTH_REQUIRED)
        String title,
        @NotBlank(message = EventValidationMessage.DESCRIPTION_MUST_NOT_BE_BLANK)
        @Size(min = EventConstraints.DESCRIPTION_MIN_LENGTH,
                max = EventConstraints.DESCRIPTION_MAX_LENGTH,
                message = EventValidationMessage.DESCRIPTION_LENGTH_REQUIRED)
        String description,
        @NotBlank(message = EventValidationMessage.AUTHOR_MUST_NOT_BE_BLANK)
        @Size(min = EventConstraints.AUTHOR_MIN_LENGTH,
                max = EventConstraints.AUTHOR_MAX_LENGTH,
                message = EventValidationMessage.AUTHOR_LENGTH_REQUIRED)
        String author
) {
}
