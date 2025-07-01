package org.csc.chessclub.dto.tournament;

import org.csc.chessclub.exception.validation.uuid.ValidUUID;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTournamentDto(
        @ValidUUID
        UUID uuid,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        UUID eventId
) {
}
