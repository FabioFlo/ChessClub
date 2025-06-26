package org.csc.chessclub.dto.tournament;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTournamentDto(
        UUID uuid,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        UUID eventId
) {
}
