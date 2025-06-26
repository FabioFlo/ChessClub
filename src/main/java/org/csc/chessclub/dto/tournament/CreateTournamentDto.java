package org.csc.chessclub.dto.tournament;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTournamentDto(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        UUID eventId
) {
}
