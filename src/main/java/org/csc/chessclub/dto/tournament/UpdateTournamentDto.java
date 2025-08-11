package org.csc.chessclub.dto.tournament;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.csc.chessclub.exception.validation.messages.TournamentValidationMessage;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.model.tournament.TournamentConstraints;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTournamentDto(
        @ValidUUID
        UUID uuid,
        @NotBlank(message = TournamentValidationMessage.TITLE_MUST_NOT_BE_BLANK)
        @Size(min = TournamentConstraints.TITLE_MIN_LENGTH,
                max = TournamentConstraints.TITLE_MAX_LENGTH,
                message = TournamentValidationMessage.TITLE_LENGTH_REQUIRED)
        String title,
        @NotNull(message = TournamentValidationMessage.DATE_MUST_NOT_BE_NULL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,
        @NotNull(message = TournamentValidationMessage.DATE_MUST_NOT_BE_NULL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate,
        @NotBlank(message = TournamentValidationMessage.DESCRIPTION_MUST_NOT_BE_BLANK)
        @Size(min = TournamentConstraints.DESCRIPTION_MIN_LENGTH,
                max = TournamentConstraints.DESCRIPTION_MAX_LENGTH,
                message = TournamentValidationMessage.DESCRIPTION_LENGTH_REQUIRED)
        String description,
        @Size(
                max = TournamentConstraints.WINNER_MAX_LENGTH,
                message = TournamentValidationMessage.WINNER_LENGTH_REQUIRED)
        String winner,
        UUID eventId
) {

}
