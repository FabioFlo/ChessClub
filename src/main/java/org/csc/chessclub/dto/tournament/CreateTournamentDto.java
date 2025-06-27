package org.csc.chessclub.dto.tournament;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.csc.chessclub.exception.validation.messages.TournamentValidationMessage;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTournamentDto(
        @NotBlank(message = TournamentValidationMessage.TITLE_MUST_NOT_BE_BLANK)
        String title,
        @NotNull(message = TournamentValidationMessage.DATE_MUST_NOT_BE_NULL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,
        @NotNull(message = TournamentValidationMessage.DATE_MUST_NOT_BE_NULL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate,
        @NotBlank(message = TournamentValidationMessage.DESCRIPTION_MUST_NOT_BE_BLANK)
        String description,
        UUID eventId
) {
}
