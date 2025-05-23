package org.csc.chessclub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.csc.chessclub.utils.EventValidationMessage;
import org.csc.chessclub.utils.ValidUUID;

import java.util.UUID;

public record UpdateEventDto(
        @ValidUUID(message = EventValidationMessage.UUID_MUST_BE_VALID)
        UUID uuid,
        @NotBlank(message = EventValidationMessage.TITLE_MUST_NOT_BE_BLANK)
        @Size(min = 2, max = 100, message = EventValidationMessage.TITLE_MUST_BE_BETWEEN_2_AND_100_CHARACTERS)
        String title,
        @NotBlank(message = EventValidationMessage.DESCRIPTION_MUST_NOT_BE_BLANK)
        String description,
        @NotBlank(message = EventValidationMessage.AUTHOR_MUST_NOT_BE_BLANK)
        @Size(min = 2, max = 30, message = EventValidationMessage.AUTHOR_MUST_BE_BETWEEN_2_AND_30_CHARACTERS)
        String author,
        String announcementPDF
) {
}
